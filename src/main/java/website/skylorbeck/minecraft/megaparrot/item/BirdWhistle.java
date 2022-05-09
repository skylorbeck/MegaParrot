package website.skylorbeck.minecraft.megaparrot.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import website.skylorbeck.minecraft.megaparrot.Declarar;
import website.skylorbeck.minecraft.megaparrot.entity.MegaParrotEntity;

public class BirdWhistle extends Item {
    public BirdWhistle(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient && itemStack.isOf(Declarar.BIRD_WHISTLE)){
            NbtCompound nbtCompound = itemStack.getOrCreateNbt();
            if (nbtCompound!=null && nbtCompound.contains("bird")){
                BlockPos pos = user.getBlockPos();
                user.playSound(SoundEvents.ENTITY_DOLPHIN_PLAY,SoundCategory.PLAYERS,1f,0.5f);
                Entity bird = ((ServerWorld)world).getEntity(nbtCompound.getUuid("bird"));
                if (bird!=null) {
                    bird.setPos(pos.getX(), pos.getY()+1, pos.getZ());
                    user.sendMessage(Text.of("Bird summoned"),true);
                } else {
                    user.sendMessage(Text.of("Your bird was unable to hear you"),true);
                }
            }
            user.getItemCooldownManager().set(this,100);
        }
        return super.use(world, user, hand);
    }


    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity.getType().equals(Declarar.MEGA_PARROT_ENTITY_TYPE)){
            NbtCompound nbtCompound = stack.getOrCreateNbt();
            nbtCompound.putUuid("bird",entity.getUuid());
            stack.writeNbt(nbtCompound);
            user.sendMessage(Text.of("Bird Registered"),true);
        }

        return super.useOnEntity(stack, user, entity, hand);
    }
}
