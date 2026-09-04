package com.gizmo.twilightgourmet.datagen.data;

import com.gizmo.twilitgourmet.TwilitGourmet;
import com.gizmo.twilitgourmet.init.GourmetLootTables;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.loot.TFLootTables;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootModifierGenerator extends GlobalLootModifierProvider {

	public LootModifierGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, TwilitGourmet.MODID);
	}

	@Override
	protected void start() {
		this.add("scavenging_ham_from_boar", this.addItemOnKnifeKill(ModItems.HAM.get(), false, 0.5F, TFEntities.BOAR.get()));
		this.add("scavenging_smoked_ham_from_boar", this.addItemOnKnifeKill(ModItems.SMOKED_HAM.get(), true, 0.5F, TFEntities.BOAR.get()));
		this.add("scavenging_leather_from_deer", this.addItemOnKnifeKill(Items.LEATHER, TFEntities.DEER.get()));
		this.add("scavenging_hide_from_dwarf_rabbit", this.addItemOnKnifeKill(Items.RABBIT_HIDE, TFEntities.DWARF_RABBIT.get()));
		this.add("scavenging_feather_from_penguin", this.addItemOnKnifeKill(Items.FEATHER, TFEntities.PENGUIN.get()));
		this.add("scavenging_string_from_tf_spiders", this.addItemOnKnifeKill(Items.STRING, TFEntities.HEDGE_SPIDER.get(), TFEntities.SWARM_SPIDER.get(), TFEntities.KING_SPIDER.get()));

		this.add("add_giant_apple_to_leaves", new AddTableLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(TwilightForestMod.prefix("blocks/giant_leaves")).build()}, GourmetLootTables.GIANT_APPLE));
		this.add("add_ice_knives_to_aurora_palace", this.addNewLootPool(TFLootTables.AURORA_ROOM, GourmetLootTables.AURORA_PALACE_INJECTION));
		this.add("add_knightmetal_knives_to_stronghold", this.addNewLootPool(TFLootTables.STRONGHOLD_CACHE, GourmetLootTables.STRONGHOLD_INJECTION));

		this.add("add_seeds_to_druid_hut", this.addNewLootPool(TFLootTables.BASEMENT, GourmetLootTables.SEED_INJECTION));
		this.add("add_seeds_to_hedge_maze", this.addNewLootPool(TFLootTables.HEDGE_MAZE, GourmetLootTables.SEED_INJECTION));
		this.add("add_seeds_to_trees", this.addNewLootPool(TFLootTables.TREE_CACHE, GourmetLootTables.SEED_INJECTION));
		this.add("add_seeds_to_fallen_trunk", this.addNewLootPool(TFLootTables.FALLEN_TRUNK_LOOT, GourmetLootTables.SEED_INJECTION));
		this.add("add_seeds_to_small_hill", this.addNewLootPool(TFLootTables.SMALL_HOLLOW_HILL, GourmetLootTables.SEED_INJECTION));
		this.add("add_seeds_to_medium_hill", this.addNewLootPool(TFLootTables.MEDIUM_HOLLOW_HILL, GourmetLootTables.SEED_INJECTION));
		this.add("add_seeds_to_large_hill", this.addNewLootPool(TFLootTables.LARGE_HOLLOW_HILL, GourmetLootTables.SEED_INJECTION));
		this.add("add_seeds_to_troll_caves", this.addNewLootPool(TFLootTables.TROLL_GARDEN, GourmetLootTables.SEED_INJECTION));
	}

	private AddTableLootModifier addNewLootPool(ResourceKey<LootTable> lootToAddTo, ResourceKey<LootTable> newPool) {
		return new AddTableLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(lootToAddTo.location()).build()}, newPool);
	}

	private AddItemModifier addItemOnKnifeKill(ItemLike item, EntityType<?>... entity) {
		return this.addItemOnKnifeKill(item, null, 1.0F, entity);
	}

	private AddItemModifier addItemOnKnifeKill(ItemLike item, @Nullable Boolean onFire, float chance, EntityType<?>... entity) {
		LootItemCondition.Builder[] entityConditions = new LootItemCondition.Builder[entity.length];

		for(int i = 0; i < entity.length; ++i) {
			entityConditions[i] = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().of(entity[i]).build());
		}

		List<LootItemCondition> conditions = new ArrayList<>();
		conditions.add(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of(ModTags.Items.KNIVES)).build()).build()).build());
		conditions.add(entityConditions.length > 1 ? AnyOfCondition.anyOf(entityConditions).build() : entityConditions[0].build());
		if (onFire != null) {
			conditions.add(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(onFire))).build());
		}

		if (chance < 1.0F) {
			conditions.add(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, chance, 0.1F).build());
		}

		return new AddItemModifier(conditions.toArray(LootItemCondition[]::new), item.asItem(), 1);
	}
}
