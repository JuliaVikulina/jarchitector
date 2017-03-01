package org.dandj.jm3client;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.shadow.SpotLightShadowFilter;
import com.jme3.shadow.SpotLightShadowRenderer;

/**
 * We use physics to make the walls and floors of a town model solid.
 * We create a solid first-person player (camera) to walk around in our town.
 *
 * @author Normen, Zathras
 */
public class ClientMain extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private Node playerNode;
    private BetterCharacterControl playerControl;
    private SpotLight spot;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private Vector3f viewDirection = new Vector3f(0, 0, 1);
    private boolean rotateLeft = false, rotateRight = false,
            strafeLeft = false, strafeRight = false,
            forward = false, backward = false;

    public static void main(String[] args) {
        ClientMain app = new ClientMain();
        app.start();
    }

    public void simpleInitApp() {
        initPhysics();
        initLight();
        initNavigation();
        initScene();
        initCharacter();
        initEffects();
        initCamera();
    }

    /**
     * Initialize the physics simulation
     */
    private void initPhysics() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true); // collision shapes visible
    }

    /* Set up collision detection for 1st-person player.
     * The player control itself has no visible geometry or location:
     * for a 3rd-person player, attach a geometry to the playerNode. */
    private void initCharacter() {
        // 1. Create a player node.
        playerNode = new Node("the player");
        playerNode.setLocalTranslation(new Vector3f(0, 0, 0));
//        PointLight light = new PointLight(Vector3f.ZERO, ColorRGBA.Red, 50f);
//        rootNode.addLight(light);
//        playerNode.addControl(new LightControl(light));
        rootNode.attachChild(playerNode);
        // 2. Create a Character Physics Control.
        playerControl = new BetterCharacterControl(1.5f, 4, 30);
        // 3. Set some properties of Character Physics Control
        playerControl.setJumpForce(new Vector3f(0, 300, 0));
        playerControl.setGravity(new Vector3f(0, -10, 0));
        // 4. Add the player control to the PhysicsSpace
        playerNode.addControl(playerControl);
        bulletAppState.getPhysicsSpace().add(playerControl);

    }

    /**
     * CameraNode depends on playerNode. The camera follows the player.
     */
    private void initCamera() {
        CameraNode camNode = new CameraNode("CamNode", cam);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        camNode.setLocalTranslation(new Vector3f(0, 4, 0));
        Quaternion quat = new Quaternion();
        quat.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
        camNode.setLocalRotation(quat);
        playerNode.attachChild(camNode);
        camNode.setEnabled(true);
        flyCam.setEnabled(false);
        flyCam.setDragToRotate(true);
    }

    /**
     * Load a model with floors and walls and make them solid.
     */
    private void initScene() {
        // make the sky blue
        viewPort.setBackgroundColor(ColorRGBA.Blue);
        // 1. Load the scene node
//        assetManager.registerLocator("town.zip", ZipLocator.class);
        assetManager.registerLocator("/home/daniil/IdeaProjects/jarchitector/target/levels", FileLocator.class);
        Node sceneNode;
//        sceneNode = (Node) assetManager.loadModel("main.scene");
//        sceneNode.scale(1.5f);
//        rootNode.attachChild(sceneNode);
        // 2. Create a RigidBody PhysicsControl with mass zero
        // 3. Add the scene's PhysicsControl to the scene's geometry
        // 4. Add the scene's PhysicsControl to the PhysicsSpace

        sceneNode = (Node) assetManager.loadModel("x3e2.obj");
        float s = 10f;
        sceneNode.move(-20, 0, -100);
        sceneNode.scale(s);
        RigidBodyControl scenePhy = new RigidBodyControl(0f);
        sceneNode.addControl(scenePhy);
        bulletAppState.getPhysicsSpace().add(scenePhy);
//
        rootNode.attachChild(sceneNode);
    }

    /**
     * An ambient light and a directional sun light
     */
    private void initLight() {
//        AmbientLight ambient = new AmbientLight();
//        rootNode.addLight(ambient);

        spot = new SpotLight();
        spot.setSpotRange(100);
        spot.setSpotOuterAngle(20 * FastMath.DEG_TO_RAD);
        spot.setSpotInnerAngle(15 * FastMath.DEG_TO_RAD);
        spot.setDirection(cam.getDirection());
        spot.setPosition(cam.getLocation());
        rootNode.addLight(spot);

    }

    private void initEffects() {
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

        /* Drop shadow test */
        final int SHADOWMAP_SIZE = 1024;
        SpotLightShadowRenderer dlsr = new SpotLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
        dlsr.setLight(spot);
        viewPort.addProcessor(dlsr);
        SpotLightShadowFilter dlsf = new SpotLightShadowFilter(assetManager, SHADOWMAP_SIZE);
        dlsf.setLight(spot);
        dlsf.setEnabled(true); // try true or false
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);

        /* Activate the glow effect in the hover tank's material*/
//        BloomFilter bf = new BloomFilter(BloomFilter);
//        fpp.addFilter(bf);
        viewPort.addProcessor(fpp);
    }

    /**
     * We override default fly camera key mappings (WASD), because we want to
     * use them for physics-controlled walking and jumping of the player.
     */
    private void initNavigation() {
        flyCam.setMoveSpeed(100);
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Rotate Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rotate Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Strafe Left", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("Strafe Right", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addListener(this, "Forward", "Rotate Left", "Rotate Right");
        inputManager.addListener(this, "Back", "Strafe Right", "Strafe Left", "Jump");
    }

    /**
     * Our  custom navigation actions are triggered by user input (WASD).
     * No walking happens here yet -- we only keep track of
     * the direction the user wants to go.
     */
    public void onAction(String binding, boolean isPressed, float tpf) {
        switch (binding) {
            case "Rotate Left":
                rotateLeft = isPressed;
                break;
            case "Rotate Right":
                rotateRight = isPressed;
                break;
            case "Strafe Left":
                strafeLeft = isPressed;
                break;
            case "Strafe Right":
                strafeRight = isPressed;
                break;
            case "Forward":
                forward = isPressed;
                break;
            case "Back":
                backward = isPressed;
                break;
            case "Jump":
                playerControl.jump();
                break;
        }
    }

    /**
     * First-person walking happens here in the update loop.
     */
    @Override
    public void simpleUpdate(float tpf) {
        // Get current forward and left vectors of the playerNode:
        Vector3f modelForwardDir = playerNode.getWorldRotation().mult(Vector3f.UNIT_Z);
        Vector3f modelLeftDir = playerNode.getWorldRotation().mult(Vector3f.UNIT_X);
        // Depending on which nav keys are pressed, determine the change in direction

        walkDirection.set(0, 0, 0);
        float speed = 20;
        if (strafeLeft) {
            walkDirection.addLocal(modelLeftDir.mult(speed));
        } else if (strafeRight) {
            walkDirection.addLocal(modelLeftDir.mult(speed).negate());
        }
        if (forward) {
            walkDirection.addLocal(modelForwardDir.mult(speed));
        } else if (backward) {
            walkDirection.addLocal(modelForwardDir.mult(speed).negate());
        }
        playerControl.setWalkDirection(walkDirection);
        // Depending on which nav keys are pressed, determine the change in rotation
        if (rotateLeft) {
            Quaternion rotateL = new Quaternion().fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateL.multLocal(viewDirection);
        } else if (rotateRight) {
            Quaternion rotateR = new Quaternion().fromAngleAxis(-FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateR.multLocal(viewDirection);
        }
        playerControl.setViewDirection(viewDirection);
        spot.setPosition(cam.getLocation());
        spot.setDirection(cam.getDirection());
    }

}
