package com.bobona.apri.entity;

import com.badlogic.gdx.math.MathUtils;
import com.bobona.apri.entity.tools.Damage;
import com.bobona.apri.entity.tools.StatusEffect;
import com.bobona.apri.ApriWorld;
import com.bobona.apri.physics.PhysicsBody;

public class Tile extends Entity {

    TileData tileData;

    public Tile(ApriWorld world, TileData tileData, float xPos, float yPos) {
        super(world, tileData, xPos, yPos);
        this.tileData = (TileData) entityData;
        body.setType(PhysicsBody.BodyType.STATIC);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void handleStatus(StatusEffect statusEffect) {
        switch (statusEffect.type) {
            case BURN:
                if (statusEffect.level >= tileData.minBurnLvToCatchOnFire && tileData.minBurnLvToCatchOnFire != -1) {
                    // randomly decides if the tile should be put on fire
                    if (MathUtils.random(120) == 22) {
                        addStatus(StatusEffect.Type.FIRE, statusEffect.level, -1);
                    }
                }
                break;
            case FIRE:
                damage(5 * statusEffect.level, Damage.FIRE);
                break;
        }
        super.handleStatus(statusEffect);
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return Tile.class;
    }
}
