# About Corridor
Corridor is a catch-all core plugin designed to implement numerous different features ranging from inventory control, chat formatting and more. 

# Features

## Vault
A standard player vaults mechanism (still work in progress)
#### To Do:
1. Construct database and cache layer for vaults.
2. Admin commands to open other player's vaults.
3. Configurable features such as adjustable vault slots and title.

## Player UUID Service
A cache + data storage mechanism (still work in progress) for tracking players' usernames by their UUIDs, removing the need to scan the disk for offline players or reach out to Mojang servers.
#### To Do:
1. Have a more robust mechanism to close sources to avoid memory hogging
2. Use batch inserts for performance

# To Do (General)
1. Make things multithreaded using a thread pool
2. Close sources more