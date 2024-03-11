package fr.traquolix.utils;

import fr.traquolix.skills.AbstractSkill;
import fr.traquolix.skills.LevelCalculator;
import fr.traquolix.skills.Skill;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static fr.traquolix.Main.logger;

/**
 * A utility class containing various helpful methods.
 */
public class Utils {

    /**
     * Private constructor to prevent instantiation.
     */
    private Utils() {}
    // A static counter used for debugging purposes.
    static int counter = 0;

    /**
     * Increases the static counter and prints the current value for debugging.
     */
    public static void debug() {
        counter++;
        logger.info(String.valueOf(counter));
    }

    /**
     * Converts the provided Instant object to a formatted string representing
     * the date and hour in the default system time zone.
     *
     * @param dateOfCreation The Instant object representing the date and time of creation.
     * @return A formatted string with the date and hour in the format "dd/MM/yyyy - HH:mm".
     */
    public static String getDateAndHour(Instant dateOfCreation) {
        LocalDateTime localDateTime = dateOfCreation.atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        return localDateTime.format(formatter);
    }

    /**
     * Cleans the given name by replacing underscores with spaces, removing the "Minecraft:" prefix,
     * and capitalizing each word.
     *
     * @param name The name to be cleaned.
     * @return The cleaned name with proper capitalization.
     */
    public static String cleanName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        // Replace underscores with spaces and remove "Minecraft:" prefix
        String cleanedName = name.replace("_", " ").replace("minecraft:", "").trim();

        // Capitalize each word
        String[] words = cleanedName.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(capitalizeFirstLetter(word)).append(" ");
        }

        return builder.toString().trim();
    }


    /**
     * Capitalizes the first letter of the given word.
     *
     * @param word The word to capitalize.
     * @return The word with its first letter capitalized.
     */
    public static String capitalizeFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }

        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }

    public static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    public static boolean isArmorSlot(int slot) {
        return slot >= 41 && slot <= 44;
    }

    public static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public static TextComponent concatenateComponents(List<Component> components) {
        TextComponent.Builder concatenatedComponent = Component.text();
        int size = components.size();

        for (int i = 0; i < size; i++) {
            concatenatedComponent.append(components.get(i));
            if (i < size - 1) {
                concatenatedComponent.append(Component.newline());
            }
        }

        return concatenatedComponent.build();
    }

    public static String toRomanNumeral(int num) {
        String[] M = {"", "M", "MM", "MMM"};
        String[] C = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] X = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] I = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        return (M[num/1000] + C[(num%1000)/100] + X[(num%100)/10] + I[num%10]).toUpperCase();
    }

    public static List<? extends Component> generateLoreProgressBar(AbstractSkill abstractSkill) {
        List<Component> lore = new ArrayList<>();

        double currentExperience = abstractSkill.getExperience();
        double experienceRequiredForNextLevel = LevelCalculator.getInstance().getRequiredExperienceUntilNextLevel(abstractSkill.getLevel());
        double experienceRequiredPreviousLevel = LevelCalculator.getInstance().getRequiredExperienceUntilNextLevel(abstractSkill.getLevel()-1);

        double totalExperienceOfStep = experienceRequiredForNextLevel - experienceRequiredPreviousLevel;
        double currentAdvancement = currentExperience - experienceRequiredPreviousLevel;

        double percentage = Math.floor(currentAdvancement/totalExperienceOfStep*100);

        lore.add(Component.empty());
        lore.add(Component.text("Progress:", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.space())
                .append(Component.text(String.format("%.2f", percentage) + "%", NamedTextColor.YELLOW)));
        lore.add(getProgressionBar(percentage).append(Component.text("  " + currentAdvancement, NamedTextColor.YELLOW)).append(Component.text(" / ", NamedTextColor.GOLD)).append(Component.text(totalExperienceOfStep + " ", NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC, false));

        return lore;
    }

    private static Component getProgressionBar(double percentage) {
        TextComponent.Builder builder = Component.text();
        int numberOfBars = (int)Math.floor(percentage / 5);
        for (int i = 0; i < numberOfBars; i++) {
            builder.append(Component.text(" ", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.STRIKETHROUGH, true));
        }
        for (int i = 0; i < 20 - numberOfBars; i++) {
            builder.append(Component.text(" ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.STRIKETHROUGH, true));
        }
        return builder.build();
    }
}
