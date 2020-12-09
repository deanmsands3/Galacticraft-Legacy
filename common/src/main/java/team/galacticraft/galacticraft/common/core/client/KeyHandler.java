package team.galacticraft.galacticraft.common.core.client;

import team.galacticraft.galacticraft.core.util.GCLog;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class KeyHandler
{
    private final KeyMapping[] keyBindings;
    private KeyMapping[] vKeyBindings;
    private boolean[] keyDown;
    private boolean[] repeatings;
    private boolean[] vRepeatings;
    public boolean isDummy;

    public KeyHandler(KeyMapping[] keyBindings, boolean[] repeatings, KeyMapping[] vanillaKeys, boolean[] vanillaRepeatings)
    {
        assert keyBindings.length == repeatings.length : "You need to pass two arrays of identical length";
        assert vanillaKeys.length == vanillaRepeatings.length : "You need to pass two arrays of identical length";
        this.keyBindings = keyBindings;
        this.repeatings = repeatings;
        this.vKeyBindings = vanillaKeys;
        this.vRepeatings = vanillaRepeatings;
        this.keyDown = new boolean[keyBindings.length + vanillaKeys.length];
    }

    public KeyHandler(KeyMapping[] keyBindings)
    {
        this.keyBindings = keyBindings;
        this.isDummy = true;
    }

    @Environment(EnvType.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            this.keyTick(event.type, false);
        }
        else if (event.phase == TickEvent.Phase.END)
        {
            this.keyTick(event.type, true);
        }
    }

    public void keyTick(TickEvent.Type type, boolean tickEnd)
    {
        boolean inChat = Minecraft.getInstance().screen instanceof ChatScreen;

        for (int i = 0; i < this.keyBindings.length; i++)
        {
            KeyMapping keyBinding = this.keyBindings[i];
//            int keyCode = keyBinding.getKeyCode();
//            if (keyCode == Keyboard.KEY_NONE) continue;
            boolean state = keyBinding.isDown();

//            try
//            {
//                if (!inChat)
//                {
//                    if (keyCode < 0)
//                    {
//                        keyCode += 100;
//                        state = Mouse.isButtonDown(keyCode);
//                    }
//                    else
//                    {
//                        state = Keyboard.isKeyDown(keyCode);
//                    }
//                }
//            }
//            catch (IndexOutOfBoundsException e)
//            {
//                GCLog.severe("Invalid keybinding! " + keyBinding.getKeyDescription());
//                continue;
//            }

            if (state != this.keyDown[i] || state && this.repeatings[i])
            {
                if (state)
                {
                    this.keyDown(type, keyBinding, tickEnd, state != this.keyDown[i]);
                }
                else
                {
                    this.keyUp(type, keyBinding, tickEnd);
                }
                if (tickEnd)
                {
                    this.keyDown[i] = state;
                }
            }
        }
        for (int i = 0; i < this.vKeyBindings.length; i++)
        {
            KeyMapping keyBinding = this.vKeyBindings[i];
//            int keyCode = keyBinding.getKeyCode();
//            if (keyCode == Keyboard.KEY_NONE) continue;
//            boolean state = keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
            boolean state = keyBinding.isDown();
            if (state != this.keyDown[i + this.keyBindings.length] || state && this.vRepeatings[i])
            {
                if (state)
                {
                    this.keyDown(type, keyBinding, tickEnd, state != this.keyDown[i + this.keyBindings.length]);
                }
                else
                {
                    this.keyUp(type, keyBinding, tickEnd);
                }
                if (tickEnd)
                {
                    this.keyDown[i + this.keyBindings.length] = state;
                }
            }
        }
    }

    public abstract void keyDown(TickEvent.Type types, KeyMapping kb, boolean tickEnd, boolean isRepeat);

    public abstract void keyUp(TickEvent.Type types, KeyMapping kb, boolean tickEnd);

}