package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

/**
 * Handles the drawing of the platform of rocks with end pieces and middle pieces
 * 
 * @author Kalan Kriner
 */
public class Rock extends AbstractGameObject
{
    private TextureRegion regEdge;
    private TextureRegion regMiddle;
    private int length;
    
    private final float FLOAT_CYCLE_TIME = 2.0f;
    private final float FLOAT_AMPLITUDE = 0.25f;
    private float floatCycleTimeLeft;
    private boolean floatingDownwards;
    private Vector2 floatTargetPosition;
    
    public Rock()
    {
        init();
    }
    
    /**
     * Sets up the basic rock dimensions and gives the rock assets
     */
    private void init()
    {
        dimension.set(1,1.5f);
        
        regEdge=Assets.instance.rock.edge;
        regMiddle=Assets.instance.rock.middle;
        
        //Start length of this rock
        setLength(1);
        
        floatingDownwards = false;
        floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2);
        floatTargetPosition = null;
    }
    
    /**
     * Sets length of the rock platform
     * @param length platform size
     */
    public void setLength(int length)
    {
        this.length=length;
        //Update bounding box for collision detection
        bounds.set(0,0, dimension.x * length, dimension.y);
    }
    
    /**
     * Increases length of platform
     * @param amount to increase platform by
     */
    public void increaseLength(int amount)
    {
        setLength(length+amount);
    }
    
    /**
     * Draws the left edge of the platform then draws the middle pieces determined by length size
     * and then a right edge piece is placed on the other end
     */
    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg=null;
        
        float relX=0;
        float relY=0;
        
        //Draw left edge
        reg=regEdge;
        relX-=dimension.x/4;
        batch.draw(reg.getTexture(), position.x+relX, position.y+relY, origin.x, origin.y, dimension.x/4, dimension.y, 
                scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
                false, false);
        
        //Draw middle
        relX=0;
        reg=regMiddle;
        for(int i=0; i< length; i++)
        {
            batch.draw(reg.getTexture(), position.x+relX, position.y+relY, origin.x, origin.y, dimension.x, dimension.y, 
                    scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
                    false, false);
            relX +=dimension.x;
        }
        
        //Draw right edge
        reg= regEdge;
        batch.draw(reg.getTexture(), position.x+relX, position.y+relY, origin.x+ dimension.x / 8, origin.y, dimension.x/4,
                dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(),true, false);      
    }
    
    /**
     * Updates the floating movement of the rocks
     */
    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        floatCycleTimeLeft -= deltaTime;
        //Creates a new 'center' point
        if(floatTargetPosition == null)
        {
            floatTargetPosition = new Vector2(position);
        }
        
        //Reverses the floating direction after it goes its time in 1 direction
        if(floatCycleTimeLeft<0)
        {
            floatCycleTimeLeft=FLOAT_CYCLE_TIME;
            floatingDownwards =!floatingDownwards;
            body.setLinearVelocity(0,FLOAT_AMPLITUDE * (floatingDownwards ? -1:1));
        }
        else
        {
            body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));
        }
        position.lerp(floatTargetPosition, deltaTime);
    }
}
