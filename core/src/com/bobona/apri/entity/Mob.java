package com.bobona.apri.entity;

import com.bobona.apri.ApriWorld;
import com.bobona.apri.physics.Impulse;
import com.bobona.apri.utils.Config;
import com.bobona.apri.entity.tools.Damage;
import com.bobona.apri.entity.tools.MovementController;
import com.bobona.apri.entity.tools.StatusEffect;
import com.bobona.apri.utils.Vector2;
import com.bobona.apri.visual.Transform;

public class Mob extends Entity {

    ApriWorld world;
    MobData mobData;
    MovementController controller;

    public Mob(ApriWorld world, MobData mobData, float xPos, float yPos) {
        super(world, mobData, xPos, yPos);
        this.world = world;
        this.mobData = mobData;
        controller = Config.instantiate(mobData.movementController, world, this);
        world.inputController.registerInputProcessor(controller);
        body.getImpulses().put("gravity", new Impulse(frame -> new Vector2(0, -ApriWorld.GRAVITY)));
        body.getImpulses().get("gravity").activate();
        setAnimation("idle");
    }

    @Override
    public void onDeath() {
        super.onDeath();
        controller.close();
    }

    @Override
    public void update() {
        super.update();
        // gravity
        controller.update();
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return Mob.class;
    }

    @Override
    public void handleStatus(StatusEffect statusEffect) {
        super.handleStatus(statusEffect);
        switch (statusEffect.type) {
            case BURN:
                if (world.ellapsedFrames % Math.floor(120 / statusEffect.level) == 0) {
                    damage(10, Damage.BURN);
                }
                break;
        }
    }
}
