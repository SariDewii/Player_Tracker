# Player_Tracker

**(!) Spigot Links (!)**

https://www.spigotmc.org/resources/player-tracker.89571/

**(!) Function (!)**

This tracker can be used in all gamemodes, and is installed intelligently

**(!) Configuration Files (!)**
```
prefix : "&8[&aPlayer_Tracker&8]"
format : "&7Tracking: &c{target} &a({range} Blocks)"
ptrack_name: "&aCompass &7(Right Click)"
def_compass: true
gui: true

#Variable formats available :

#{name} - Name of the player
#{target} - Target of radar
#{range} - Range between player to target in blocks
#{health} - Health of player targets
#{food} - Food level of player targets
```

**(!) ActionBar Version (!)**

1.8:
```ruby
this.packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text + "\"}"),(byte) 2);
```

1.13:
```ruby
this.packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text + "\"}"), ChatMessageType.a((byte)2));
```

`© Copyright 2021 by Varaday`
