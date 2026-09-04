package com.gizmo.twilightgourmet.datagen.data.loot;

import com.gizmo.twilitgourmet.init.GourmetBlocks;
import com.gizmo.twilitgourmet.init.GourmetItems;
import com.gizmo.twilitgourmet.init.GourmetLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.BiConsumer;

public record MiscLootTables(HolderLookup.Provider registries) implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(GourmetLootTables.AURORA_PALACE_INJECTION, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(GourmetItems.ICE_KNIFE).setWeight(3))
				.add(LootItem.lootTableItem(GourmetItems.ICE_KNIFE).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
				.add(EmptyLootItem.emptyItem().setWeight(15))));

		output.accept(GourmetLootTables.STRONGHOLD_INJECTION, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(GourmetItems.KNIGHTMETAL_KNIFE).setWeight(3))
				.add(LootItem.lootTableItem(GourmetItems.KNIGHTMETAL_KNIFE).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
				.add(EmptyLootItem.emptyItem().setWeight(15))));

		output.accept(GourmetLootTables.GIANT_APPLE, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(GourmetBlocks.GIANT_APPLE))
				.when(BonusLevelTableCondition.bonusLevelFlatChance(this.registries.holderOrThrow(Enchantments.FORTUNE), 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))));

		output.accept(GourmetLootTables.SEED_INJECTION, LootTable.lootTable().withPool(LootPool.lootPool()
				.setRolls(UniformGenerator.between(1.0F, 3.0F))
				.add(EmptyLootItem.emptyItem().setWeight(6))
				.add(LootItem.lootTableItem(ModItems.CABBAGE_SEEDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))).setWeight(3))
				.add(LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).setWeight(2))
				.add(LootItem.lootTableItem(ModItems.ROTTEN_TOMATO.get()))
				.add(LootItem.lootTableItem(ModItems.ONION.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).setWeight(2))
				.add(LootItem.lootTableItem(ModItems.RICE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))).setWeight(2))));
	}
}
