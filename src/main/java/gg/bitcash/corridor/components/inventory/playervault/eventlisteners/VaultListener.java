package gg.bitcash.corridor.components.inventory.playervault.eventlisteners;

import gg.bitcash.corridor.Corridor;

abstract class VaultListener {

    Corridor instance;

    protected VaultListener(Corridor instance) {
        this.instance = instance;
    }
}
