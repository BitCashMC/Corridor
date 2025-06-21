# About Corridor
Corridor is a catch-all core plugin designed to implement numerous different features ranging from inventory control, chat formatting and more. 

# Features

## Vault
The vault mechanism allows for players to store items in personal, virtualized chests that only they can access.

### Plan:
> There will be a data service, a Map object, that pairs all UUIDs to another Map pairing all arrays of ItemStacks to integers denoting the vault number. When the player opens a vault, the OpenCommand class will 
> query the data service based on the UUID, and will retrieve the vault pertaining to the numerical argument passed during the command.
> This is done by retrieving that itemstack array, creating an inventory and placing the items in it before delivering it to the player.
> 
> Everytime a player joins (for the first time), the plugin will listen for that and register their UUID to the data service mapping.
> 
> When a player permutates the inventory of a vault, his/her changes will be saved by another listener listening for the inventory closing. In this circumstance, the distinguishable attribute that lets the plugin know 
> that a vault was closed (and not any other inventory) is by the fact that every inventory built and delivered to the player will have a VaultIdentity instance as the InventoryHolder. Thus a simple instanceof check allows the plugin
> to only react to vault closures. 
> 
> The saving is done by fetching the existing itemstack array of the inventory from the event instance and then writing to the data service object.
> 
> **Next Stage**
> is to get the title to show the player's username and vault number at the top. Before I do that, I need to refactor
> how inventories are assembled and delivered. It feels out of place for the Data Service to be responsible for assembling
> inventories.
> 
> #### Vault Manager
> The vault manager, which possesses a VaultDataService object as state, will receive the data from the object and will construct
> the inventory to be delivered to the player. The 
> 
> **buildInventory** will package the Inventory instance to be displayed by the user.  
> 
> **TO DO**
> 1. Admin mechanism to open other people's vaults
> 2. Implement the DAO
> 3. Introduce configurability
> 4. Implement a plugin-wide database mechanism to hold players 
> 
> ## Username UUID database (GLOBAL)
> Maintain a database of UUIDs paired to their usernames that will be loaded up onto a cache layer on startup.
> There will be a DAO that handles the database connectivity. 
> 
> Essentially the process is that there will be a DAO that upon startup, loads from a database. And upon termination, 
> submits the cache to the database.
> 
> Cache Layer (PlayerDataService) will be a simple mechanism that stores a Map of all usernames to UUIDs.
> There will be an event listener for onPlayerJoin to check their username against their UUID, if there is a conflict,
> it will update the on-memory cache. 
> 
> PlayerDAO will handle the database connectivity, and the instantiation of a PlayerDataService.
> 
> Batch Insert and other performance improvements
> Stored procedures