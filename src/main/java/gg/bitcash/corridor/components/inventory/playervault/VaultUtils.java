package gg.bitcash.corridor.components.inventory.playervault;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VaultUtils {

    private VaultUtils() {}

    public static Inventory buildInventory(VaultMeta vaultMeta, String username) {
        Inventory inventory = Bukkit.createInventory(new VaultIdentity(vaultMeta),54,username + "'s Vault (#" + vaultMeta.number() + ")");
        inventory.setContents(vaultMeta.itemStacks());
        return inventory;
    }
    /**
     * Performs a double-layered serialization process to save the inventory state that the inputted ItemStack[] represents to the database. It will begin by using Bukkit's ConfigurationSerializable#serialize method to serialize each individual ItemStack into a List
     * of Map<String,Object> s. It will then serialize the entire List instance to bytecode, before giving it back as a byte array.
     * @param items The array of ItemStack s to be serialized
     * @return A byte array representing the serialized state of the List of Bukkit-serialized ItemStacks.
     */
    public static byte[] serializeInventory(ItemStack[] items) {
        /*
        Instantiate the necessary OutputStreams to receive the Bukkit-serialized list of Maps, and then to serialize that serialization into bytecode.
         */
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            List<Map<String,Object>> preSerialize = Arrays.stream(items).map(p->p != null ? p.serialize() : null).toList();
            oos.writeObject(preSerialize);
            return bos.toByteArray(); // Return the byte array
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Performs a double-layered deserialization process to, given an array of bytes, deserialize the bytecode that this byte array represents into the higher-level serialization format adopted by Bukkit's serialization mechanism. From there it will
     * individually deserialize (ItemStack.deserialize) each item into an ItemStack, before finally reducing to an array of ItemStacks that will then be returned.
     * @param serializedInventory The byte array representing the (presumably correct) serialization of the items
     * @return The reduced ItemStack array
     */
    public static ItemStack[] deserializeInventory(byte[] serializedInventory) {
        //Instantiate necessary InputStreams
        try (ByteArrayInputStream bis = new ByteArrayInputStream(serializedInventory); ObjectInputStream ois = new ObjectInputStream(bis)) {
            List<Map<String,Object>> preDeserialize = (List<Map<String, Object>>) ois.readObject();

            return preDeserialize.stream().map(p->p != null ? ItemStack.deserialize(p) : null)
                    .toList()
                    .toArray(new ItemStack[54]); //Reduce and return an ItemStack[54]
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
