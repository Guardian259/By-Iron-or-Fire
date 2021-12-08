package com.guardian.bif.mixin;

import com.guardian.bif.ByIronOrFire;
import com.guardian.bif.util.EntityAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.entity.effect.StatusEffects.*;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements EntityAccessor {

    @Shadow
    @Final
    private PlayerInventory inventory;

    //TODO: Depreciate the static integer for a variable
    //# of ticks before freezing damage occurs
    @Override
    public int getMinFreezeDamageTicks (){
        return 1000;
    }


    //TODO: this will require a Point Of Interest system for Fires, Campfries, and Soulfires for this to work properly;
    // Consider adding a minor heat effect to the holding of a torch in players hand/offhand
    /*
     * Players who are within the proximity of a fire, lava, campfire, or soulfire will have there FreezingTick's reduced back to zero;
     * can be counteracted by other freezing effects, but will alleviate the risk of fatality
     */
    @Inject(method = "tick", at = @At("HEAD"))
    public void playerWarmthTick(CallbackInfo ci){
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        World world = playerEntity.getWorld();

        /*if(!playerEntity.isTouchingWater() && playerEntity.getBlockPos() <= ){

        }*/
    }


    /*
     * Players who are touching water in cold biomes, exposed to cold biomes air,
     * and travel through cold biomes with ill-equipped armor will have there freezing ticks increase.
     * the number of ticks required for damage, amount of damage, and lethality of freezing is determined by the armor sets being worn
     */
    @Inject(method = "tick", at = @At("HEAD"))
    public void playerFreezingTick(CallbackInfo ci){
        //Current PlayerEntity and World
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        World world = playerEntity.getWorld();
        int frozenTicks = playerEntity.getFrozenTicks();


        //TODO: Complete armor check and integrate into the rest of the player freezing system
        // Will Require the completion of the FreezingRegistry
        /*Loops through the current armorList
        DefaultedList<ItemStack> armorArray = inventory.armor;
        for (int currentSlotId = 0; currentSlotId < armorArray.size(); currentSlotId++) {
            Item currentItem = armorArray.get(currentSlotId).getItem();

            if(currentItem instanceof ArmorItem){
                //check for armor types repair material and assign the appropriate minFreezeDamageTicks from Entity accordingly
                if(((ArmorItem) currentItem).getMaterial().getRepairIngredient().test(Items.LEATHER.getDefaultStack())){

                }

            } else {
                //backup code for null armor slots and modded armors that failed to be caught by the FreezingRegistry
            }
        }*/

        //Server-side check
        if(!world.isClient) {
            //Sources the current biome the player is in, then defines and initializes frozenTicks
            Biome biome = world.getBiome(playerEntity.getBlockPos());
            Float temperature = biome.getTemperature();

            /* Checks the biome temperature and sets the players freezing ticks based on what armor is worn, if any
             * Effects Windswept Hills, Windswept Forest, Windswept Gravelly Hills, Stony Shore and colder biomes*/
            if(temperature <= 0.2f){
                //check for if the player is touching water and doesn't have the conduit power status effects
                inIcyWater = ((playerEntity.isTouchingWater()) && (!playerEntity.hasStatusEffect(CONDUIT_POWER)));
                /* Checks the biome temperature and sets the players freezing ticks based on what armor is worn, if any
                 * Effects Snowy Beach and colder biomes*/
                inIcyBiome = ((temperature <= 0.05f) && world.isSkyVisible(playerEntity.getBlockPos()));
            }
            /*Checks the biome weather for snowfall and sets the players freezing ticks based on what armor is worn, if any
             * Base FrozenTick rate from snowfall alone is half of all other points
             * Effects Snowy Beach and colder biomes and when the player is above the raining line in temperate biomes*/
            inSnowstorm = ((biome.getPrecipitation() == Biome.Precipitation.SNOW) && world.isRaining());
        }
    }

    /*=================================================================================================================
     *Armor Visibility Check
     *=================================================================================================================*/

    /*
     * Players current armor is looped through and used to determine current player visibility.
     * Visibility is used to adjust nameplate view distance and mob tracking lock-on distance.
     */
    @Inject(method = "tick", at = @At("HEAD"))
    public void playerVisibilityTick(CallbackInfo ci) {

        DefaultedList<ItemStack> armorArray = inventory.armor;

        //Loops through the current armorList
        for (int currentSlotId = 0; currentSlotId < armorArray.size(); currentSlotId++) {
            Item currentItem = armorArray.get(currentSlotId).getItem();
            int[] visibilityRanges;
            int itemVisibility;

            if (currentItem instanceof ArmorItem) {
                /*Compares the armor material with a corresponding vanilla int[] array; Will currently throw an error for modded armor materials*/
                ArmorMaterial key = ((ArmorItem) currentItem).getMaterial();
                visibilityRanges = ByIronOrFire.ARMORVISIBILITY.get(key);
                itemVisibility = visibilityRanges[currentSlotId];
                /*sets the current visibility slot with the value in the corresponding array*/
                setEntityVisibility(currentSlotId, itemVisibility);
            } else {
                /*For default case, as well as, all VANILLA unknowns; This will happily function for all mob types, including mobs that do not wear armor*/
                visibilityRanges = ByIronOrFire.ARMORVISIBILITY.get(ArmorMaterials.IRON);
                itemVisibility = visibilityRanges[currentSlotId];
                /*sets the current visibility slot with either {8,24,20,12} for a sum of 64, the default minecraft visibility value */
                setEntityVisibility(currentSlotId, itemVisibility);
            }
        }
    }

}
