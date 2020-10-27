# SideSurvivalPortals
Custom portal plugin for Side Survival server.

The plugin is based on Side public SMP 3rd Season's portal system where every player can create portals (in form of Nether portals) to get around the server more easily.

## Plugin features
Portals are directly linked to [Lands](https://www.spigotmc.org/resources/53313/) plugin.

### Portal creation
When a player attempts to open a Nether portal, the plugin takes over and places a registered custom Portal in its place.
With Lands integration, there is a permission/setting check (whether the player can create portal) in form of `ManagementSetting.AREA_ASSIGN` setting from Lands plugin (for the player's role in the land where the portal is located in).

Currently plugin uses flatfile database (YAML) in readable format (located in `plugins/SideSurvivalPortals/portals`).

As almost all interaction with Portal is handled with GUI, actual portal blocks are replaced with Purple Staind Glass Pane.

![Portal creation](https://i.imgur.com/rxRp5eJ.jpg)

**Important:** Portal is only registered by its glass panes - it is possible to remove obsidian blocks and make your own portal designs!

### Main menu
![Main portal menu](https://i.imgur.com/fEwfNSV.jpeg)

When entering any portal on the server, the main Portal menu appears. There are always 3 options available:

1. Go to the Nether (or Spawn, depending on current dimension)
2. Private portals
3. Public portals

If the player has permission/setting `ManagementSetting.WAR_MANAGE`, the fourth option appears (Portal options) and they can break the portal (which will delete all its saved options).

### Portal settings
![Portal settings](https://i.imgur.com/ny6O5WH.jpeg)

In the settings menu player can:

1. **Change access options**

This will open another GUI where the player can switch between:

  - Whether the portal is public (or private - only available for linked Land members and other specified objects);
  - Which Lands can access the portal (if it is private).
    This opens another GUI where you can remove access.
    To give another land access to the portal, the player needs to run a command `/p pievienot t <land name>`.
  - Which players (separately) can access the portal (if it is private).
    This opens another GUI where you can remove access.
    To give another land access to the portal, the player needs to run a command `/p pievienot s <online player>`.

2. **Change description**

Every portal can have its own description (max 90 characters), that can be set with a command `/p apraksts <description>`.

3. **Change icon**

As a default, portals show up as "papers" in Private and Public portal lists. This can be changed, there are 54 (max) icons that can be customized in the plugin's config file.

This menu option opens another GUI where the player can change the icon.

### Private portals
In the Private portals GUI, the player has an option to teleport to all portals he has access to. This includes:

- All portals from lands he is part of;
- All portals where any of the lands he is part of has access of;
- All portals where other lands have given access to the player.

### Public portals
As the name suggests, every portal that is marked as "public" (every portal is private as default) can be found here.

![Public portals](https://i.imgur.com/lrIzkKX.jpeg)

## Other stuff
Currently, there are no limits on how many portals a player can make but in the future, there might be a possibility to limit this.

When teleporting to somewhere, portal glass blocks are replaced (client-side) with actual portal blocks, and after 4 seconds player gets teleported.

Currently, every message is hardcoded but in the following days, all messages will be changeable in the config.

## Development stuff
The only necessary dependencies are WorldGuard and Lands.

The plugin is compiled using Maven, check pom.xml for more info.