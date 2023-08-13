package fr.traquolix.mercenaries.JackTheRipper;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.mercenaries.AbstractMercenary;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.PlayerSkin;

@Getter
public class JackTheRipper extends AbstractMercenary {
    public static final Identifier identifier = new Identifier("mercenary", "jack_the_ripper");
// TODO For a nemesis system, it must be quite rare.
    public JackTheRipper() {
        super();
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    @Override
    public PlayerSkin getSkin() {
        return new PlayerSkin("ewogICJ0aW1lc3RhbXAiIDogMTY5MTg3NzI4MDM2NywKICAicHJvZmlsZUlkIiA6ICI2NTc3OGE5YWUzYTE0MTI5ODVlN2RjNTdhMzc3NTE1YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYXJ0b3BoIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVjNTY5MmUwZTUzNTRiM2VmYjUwYWM3MTc0NmY0OWVlMDljZTVhNWQ0NmJhMWE3ZmI1YmE1MWYyOTNhYmNiMjgiCiAgICB9CiAgfQp9",
        "Hm75nYllS9ygrL3lvzh9jxvUIEe7B7vfwe6N2CKiIWBn/KC2EiC+JjgVEdHW9NYPVoUtJbZH4i0piqVSh/DIkxtlNLvZVQqvFd47b4n/VYxddssozW+5jjDhsKTYnfUMVq3rbK1hs6ricKqXapZ7nH7YzD//JfRmSrlZXjXNCJEzO1GopNYCSXDApFBP0bB9MtVG+74rnGAZ/QMY/vemqtDuGqkuBAlnw69nNwHOpKCMbypm9z3mGcLQxHEXVdUQR4waBYFmDok58NBRf42giBSweMD33nEs4CsGafl1JRek1UGnzlkD5j0P9IalMHB33jE4yFabxOcNt4PsZi97rKjT75R3OUldq8bTf9YkQL2t/cmVzEpbpFANXGG9KvGHFV24SO+Txmy/NkzYsGo8p09Pue2vHOm6sOkfHIKEL+OvU6vzG7h/MHl85BtE+/xFIqTSExluUlT2eWMeuYGAq3nh2Mh0EHM2dkk39N41eoREG/UEDL+/N4BkgWMtYrewywFi/y3S9DPSA+0Qvef94EiLnsBJvMk9Cc9heX0Fw+lHpJB9w+Jh/kSfCTtoosUQ9cBq7EP9ZK8vgsVGGfNAV/h/TiK8aoGgNIdchLnY/IYQfTDlFTcb+RuENLstz8e8YAaoAuvAjChKvCkds+Wfx2yErCSChmom3goFKOno3V8=");
    }


    @Override
    public Component getDisplayName() {
        return Component.text("Jack the Ripper");
    }
}
