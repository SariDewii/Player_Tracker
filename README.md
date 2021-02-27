# Tracker

**(!) Spigot Links (!)**

https://www.spigotmc.org/resources/player-tracker.89571/

**(!) Function (!)**

This tracker can be used in all gamemodes, and is installed intelligently

**(!) Configuration Files (!)**
```
prefix : "&8[Tracker&8]"
customrecipe : true
format : "&7Tracking: &c{target} &a({range} Blocks)"
track:
  name: "&aCompass &7(Right Click)"
gui: true
defaultcompass: true

#Custom recipe enables you to create a radar by putting compass in a crafting grid
#Available format variables:
#{name} - Name of the player
#{target} - Target of radar
#{range} - Range between player to target in blocks
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
