package fr.traquolix.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    public static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }


}
