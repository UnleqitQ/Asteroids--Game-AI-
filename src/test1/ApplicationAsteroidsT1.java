package asteroids.test1;

import static org.lwjgl.system.MemoryStack.*;
import org.lwjgl.system.*;
import org.neat.activationFunction.ActivationFunction;
import org.neat.calculate.Calculator;
import org.neat.genome.Genome;
import org.neat.libraries.Entry;
import org.neat.neat.Neat;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class ApplicationAsteroidsT1 extends Window {
	
	ApplicationAsteroidsT1 self;
	
	List<Genome> bestGenomes = new ArrayList<>();
	long window;
	Neat neat;
	ShowBestGenome showBestGenome;
	Thread showBestGenomeThread;
	boolean show = true;
	boolean showBestGenomes = false;
	float fps = 100;
	
	public static void main(String[] args) {
		ApplicationAsteroidsT1 applicationAsteroidsT1 = new ApplicationAsteroidsT1();
	}
	
	public ApplicationAsteroidsT1() {
		self = this;
		init();
		loop();
	}
	
	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, 0);
		glfwWindowHint(GLFW_RESIZABLE, 1);
		glfwWindowHint(GLFW_DECORATED, 0);
		window = glfwCreateWindow(1920/1, 1080/1, "Win", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(window, pWidth, pHeight);
			
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			glfwSetWindowPos(window, (vidMode.width()-pWidth.get(0))/2, (vidMode.height()-pHeight.get(0))/2);
		}
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
		glfwSetKeyCallback(window, new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_N) {
					if (action==1) {
						show = !show;
						if (showBestGenomes) {
							show = false;
						}
					}
				}
				if (key == GLFW_KEY_PAGE_UP) {
					if (action==1) {
						if (fps<500) {
							fps *= 2;
						}
					}
				}
				if (key == GLFW_KEY_PAGE_DOWN) {
					if (action==1) {
						if (fps>1) {
							fps /= 2;
						}
					}
				}
				/*if (key == GLFW_KEY_Q) {
					if (action==1) {
						showBestGenomes = !showBestGenomes;
						show = !showBestGenomes;
						if (showBestGenomes) {
							showBestGenome = new ShowBestGenome(self);
							showBestGenomeThread = new Thread(showBestGenome);
							showBestGenomeThread.setDaemon(true);
							showBestGenomeThread.start();
						}
						else {
							showBestGenomeThread.interrupt();
						}
					}
				}*/
			}
		});
	}
	
	private void loop() {
		neat = new Neat(8, 3, true);
		
		float mul = 0.2f;
		
		neat.config.initGenome.meshPercentage = 0.03f;
		
		neat.config.crossover.takeSecondNode = 0.3f;
		neat.config.crossover.takeSecondConnection = 0.3f;
		neat.config.crossover.takeSecondActivation = 0.3f;
		//neat.config.crossover.takeSecondAggregation = 0.3f;
		//neat.config.crossover.thinConnections = true;
		neat.config.crossover.thinProbOrPerc = 0.2f;
		neat.config.crossover.thinViaPercentage = false;
		
		neat.config.species.stagnationDuration = 10;
		
		neat.config.mutate.addExistingNode = true;
		neat.config.mutate.nodeActivation = true;
		neat.config.mutate.genomeActivation = true;
		neat.config.mutate.averageAddNodesPerMutate = 0.6f*mul;
		neat.config.mutate.averageAddExistingNodesPerMutate = 0.075f*mul;
		neat.config.mutate.averageAddConnectionsPerMutate = 0.08f*mul;
		neat.config.mutate.averageNewNodesActivationPerMutate = 0.005f*mul;
		neat.config.mutate.averageToggleNodesPerMutate = 0.0375f*mul;
		neat.config.mutate.averageToggleConnectionsPerMutate = 0.04f*mul;
		neat.config.mutate.averageCombineInConnectionsPerMutate = 0.09f*mul;
		neat.config.mutate.averageCombineOutConnectionsPerMutate = 0.09f*mul;
		neat.config.mutate.genomeActivationProb = 0.005f*mul;
		
		neat.config.mutate.newLinkWeightInterval = 0.2f;
		neat.config.mutate.mutateWeightInterval = 0.04f;
		
		//neat.activationFunctions.add(ActivationFunction.Step);
		//neat.activationFunctions.add(ActivationFunction.Clamped);
		//neat.activationFunctions.add(ActivationFunction.Gauss);
		neat.activationFunctions.add(ActivationFunction.Hat);
		//neat.activationFunctions.add(ActivationFunction.Sin);
		//neat.activationFunctions.add(ActivationFunction.Tanh);
		//neat.activationFunctions.add(ActivationFunction.ReLU);
		//neat.activationFunctions.add(ActivationFunction.SiLU);
		
		neat.initialize();
		
		neat.createGenomes(25);
		neat.mutate();
		neat.breedrand(475);
		neat.mutate();
		
		neat.initSpecicateRand();
		neat.specicate();
		
		GL.createCapabilities();
		glClearColor(0, 0, 0, 0);
		
		
		while (!glfwWindowShouldClose(window)) {
			boolean run = true;
			List<Game> games = new ArrayList<>();
			List<Calculator> calculators = neat.createCalculators();
			for (Calculator calculator : calculators) {
				games.add(new Game(calculator, this));
			}
			
			while (run&!glfwWindowShouldClose(window)) {
				if (show|showBestGenomes) glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
				if (show|showBestGenomes) glColor3f(1, 1, 1);
				run = false;
				for (Game game : games) {
					if (game.ship.alive) {
						run = true;
						break;
					}
				}
				glLineWidth(2);
				if (show&!showBestGenomes) {
					for (Game game : games) {
						drawGame(game);
					}
				}
				if (showBestGenomes&&showBestGenome.game!=null) {
					drawGame(showBestGenome.game);
				}
				for (Game game : games) {
					game.tick(0.02f);
				}
				if (show|showBestGenomes) glfwSwapBuffers(window);
				glfwPollEvents();
				if (show|showBestGenomes) {
					try {
						Thread.sleep((long)(1000/fps));
					}
					catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			}
			
			neat.sortGenomes();
			neat.calcSpeciesFitness();
			neat.sortSpecies();
			System.out.println(neat.genomes.getList().get(0).fitness);
			neat.cutGenomesInSpecies(0.9f);
			neat.breedSpeciesUpTo(500);
			bestGenomes.add(neat.genomes.getList().get(0));
			neat.mutate();
			neat.clearGenomesOfSpecies();
			neat.specicate();
			neat.removeEmptySpecies();
			
		}
	}
}
