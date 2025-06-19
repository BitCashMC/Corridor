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
> **STEPS**
> 1. Get the command base framework. -- DONE
> 2. Get the data service object working with the right read/write functions for the command to ultimately use.
> 3. Create the inventory closure listener and have it do the aforementioned operations.
> 
> **TO DO**
> 1. Create a write function for the VaultDataService
> 2. Create a listener that will save the vault upon closure