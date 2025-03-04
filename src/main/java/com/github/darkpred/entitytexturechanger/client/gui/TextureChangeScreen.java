package com.github.darkpred.entitytexturechanger.client.gui;

import com.github.darkpred.entitytexturechanger.capabilities.ITexReplacementCap;
import com.github.darkpred.entitytexturechanger.capabilities.ModCapabilities;
import com.github.darkpred.entitytexturechanger.network.C2SSubmitTextureMessage;
import com.github.darkpred.entitytexturechanger.network.MessageHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

public class TextureChangeScreen extends Screen {
    private final Entity target;
    private Button confirmButton;
    private Button clearButton;
    private EditBox linkBox;

    public TextureChangeScreen(Entity target) {
        super(new TextComponent("Marker"));
        this.target = target;
    }

    @Override
    protected void init() {
        super.init();
        confirmButton = addRenderableWidget(new Button(width / 2 - 100, height / 4 + 96 + 12, 200, 20, new TranslatableComponent("markerscreen.confirm"), button -> {
            MessageHandler.INSTANCE.sendToServer(new C2SSubmitTextureMessage(target.getId(), linkBox.getValue()));
            minecraft.setScreen(null);
        }));
        clearButton = addRenderableWidget(new Button(width / 2 - 100, height / 4 + 96 + 32, 200, 20, new TranslatableComponent("markerscreen.clear"), button -> {
            MessageHandler.INSTANCE.sendToServer(new C2SSubmitTextureMessage(target.getId(), ""));
            minecraft.setScreen(null);
        }));
        linkBox = addRenderableWidget(new EditBox(font, width / 2 - 100, 116, 200, 20, new TranslatableComponent("addServer.enterName")));
        linkBox.setFocus(true);
        linkBox.setMaxLength(2000);
        setFocused(linkBox);
        linkBox.setResponder(string -> updateSelectButtonStatus());
        updateSelectButtonStatus();
    }

    private void updateSelectButtonStatus() {
        confirmButton.active = false;
        try {
            new URL(linkBox.getValue()).toURI();
            confirmButton.active = true;
        } catch (MalformedURLException | URISyntaxException ignored) {
        }
        Optional<ITexReplacementCap> opt = ModCapabilities.getMarkerCap(target);
        clearButton.active = opt.isPresent() && opt.get().hasTexture();
    }
}
