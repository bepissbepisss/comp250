import assignment1.*;

import org.junit.jupiter.api.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MiniTest {

	///
	/// Competency level calculations
	///

	enum COMPETENCY { P, AM, M };

	 private static Map<COMPETENCY, Double> totalTests = new HashMap<>(Map.of(
			  COMPETENCY.P, 0.0,
			  COMPETENCY.AM, 0.0,
			  COMPETENCY.M, 0.0
			 ));

	private static Map<COMPETENCY, Double> passedTests = new HashMap<>(Map.of(
			COMPETENCY.P, 0.0,
			COMPETENCY.AM, 0.0,
			COMPETENCY.M, 0.0
			));
	
	private static boolean proficiency;
	private static boolean approachingMastery;
	
	@Test
	@Tag("score:0")
	@DisplayName("BASIC from EXPOSED Tests Achieved: Requires >50% proficiency tests to pass")
	@Order(1004)
	void testBasicLevelPassed() {
		int basicThreshold = (int) Math.ceil(0.50 * totalTests.get(MiniTest.COMPETENCY.P));
		assertTrue(passedTests.get(MiniTest.COMPETENCY.P) >= basicThreshold,
				"Basic competency not achieved. Required: " + basicThreshold + " Passed: " + passedTests.get(MiniTest.COMPETENCY.P));
		System.out.println("Basic competency achieved: " + passedTests.get(MiniTest.COMPETENCY.P) + " out of " + totalTests.get(MiniTest.COMPETENCY.P) + ". Required was: " + basicThreshold);
	}
	
	@Test
	@Tag("score:0")
	@DisplayName("PROFICIENCY from EXPOSED Tests Achieved: Requires >85% proficiency tests to pass")
	@Order(1005)
	void testProficiencyLevelPassed() {
		int proficiencyThreshold = (int) Math.ceil(0.85 * totalTests.get(MiniTest.COMPETENCY.P));
		proficiency = passedTests.get(MiniTest.COMPETENCY.P) >= proficiencyThreshold;
		assertTrue(proficiency,
				"MiniTest Proficiency competency not achieved. Required: " + proficiencyThreshold + " Passed: " + passedTests.get(MiniTest.COMPETENCY.P));
		System.out.println("MiniTest Proficiency competency achieved: " + passedTests.get(MiniTest.COMPETENCY.P) + " out of " + totalTests.get(MiniTest.COMPETENCY.P) + ". Required was: " + proficiencyThreshold);
	}
	
	@Test
	@Tag("score:0")
	@DisplayName("APPROACHING MASTERY from EXPOSED Tests Achieved: Requires >85% approaching mastery tests to pass")
	@Order(1006)
	void testApproachingMasteryLevelPassed() {
		int approachingMasteryThreshold = (int) Math.ceil(0.85 * totalTests.get(MiniTest.COMPETENCY.AM));
		approachingMastery = passedTests.get(MiniTest.COMPETENCY.AM) >= approachingMasteryThreshold;
		assertTrue(proficiency && approachingMastery,
				"MiniTest Approaching Mastery competency not achieved. Required: " + approachingMasteryThreshold + " Passed: " + passedTests.get(MiniTest.COMPETENCY.AM));
		System.out.println("MiniTest Approaching Mastery competency achieved: " + passedTests.get(MiniTest.COMPETENCY.AM) + " out of " + totalTests.get(MiniTest.COMPETENCY.AM) + ". Required was: " + approachingMasteryThreshold);
	}
	
	@Test
	@Tag("score:0")
	@DisplayName("MASTERY LEVEL from EXPOSED Tests Achieved: Requires >85% mastery tests to pass")
	@Order(1007)
	void testFullMasteryLevelPassed() {
		int fullMasteryThreshold = (int) Math.ceil(0.85 * totalTests.get(MiniTest.COMPETENCY.M));
		assertTrue(proficiency && approachingMastery && passedTests.get(MiniTest.COMPETENCY.M) >= fullMasteryThreshold,
				"MiniTest Mastery competency not achieved. Required: " + fullMasteryThreshold + " Passed: " + passedTests.get(MiniTest.COMPETENCY.M));
		System.out.println("MiniTest Mastery competency achieved: " + passedTests.get(MiniTest.COMPETENCY.M) + " out of " + totalTests.get(MiniTest.COMPETENCY.M) + ". Required was: " + fullMasteryThreshold);
	}
	
	///
	/// Helper methods
	///

	private static Class<?> getPrimitiveIfPossible(Class<?> clazz) {
		if (clazz == Integer.class) return int.class;
		if (clazz == Double.class) return double.class;
		if (clazz == Boolean.class) return boolean.class;
		if (clazz == Long.class) return long.class;
		if (clazz == Short.class) return short.class;
		if (clazz == Byte.class) return byte.class;
		if (clazz == Float.class) return float.class;
		if (clazz == Character.class) return char.class;
		return clazz;
	}

	private Tile createTileOnPath() {
		/*return new Tile() {
            @Override
            public boolean isOnThePath() {
                return true;
            }
        };*/
		return new Tile(false, false, true, null, null, null, new MonsterTroop());
	}

	private Class<?> getCanonicalType(Object arg) {
		// If the argument is a Tile (or subclass), return Tile.class
		if (arg instanceof Tile) {
			return Tile.class;
		}
		// For numbers, return the primitive type if available
		return getPrimitiveIfPossible(arg.getClass());
	}

	private void checkForForbiddenInstance(Object object) {
		Class<?> clazz = object.getClass();

		// Check fields for forbidden instances
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			checkFieldForForbiddenInstance(field);
		}

		// Check methods for forbidden instances
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (!Modifier.isStatic(method.getModifiers())) {
				// Exclude static methods
				checkMethodForForbiddenInstance(method);
			}
		}
	}

	private void checkFieldForForbiddenInstance(Field field) {
		Class<?> fieldType = field.getType();
		if (isForbiddenType(fieldType)) {
			fail(fieldType.getName() + " instance found in the class " + field.getDeclaringClass().getName());
		}
	}

	private void checkMethodForForbiddenInstance(Method method) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		for (Class<?> paramType : parameterTypes) {
			if (isForbiddenType(paramType)) {
				fail(paramType.getName() + " instance found in the class " + method.getDeclaringClass().getName());
			}
		}
	}

	private boolean isForbiddenType(Class<?> type) {
		return type == java.util.ArrayList.class
				|| type == java.util.LinkedList.class
				|| type == HashMap.class
				|| type == List.class
				|| type == Map.class
				|| type == java.util.Set.class
				|| type == java.util.HashSet.class
				|| type == java.util.TreeSet.class
				|| type == java.util.Queue.class
				|| type == java.util.PriorityQueue.class
				|| type == java.util.Stack.class
				|| type == java.util.Vector.class
				|| type == java.util.concurrent.ConcurrentHashMap.class
				|| type == java.util.concurrent.CopyOnWriteArrayList.class
				|| type == java.util.concurrent.CopyOnWriteArraySet.class
				|| type == java.util.concurrent.LinkedBlockingQueue.class;
	}

	private void checkPublicStaticField(Class<?> clazz, String fieldName, Class<?> expectedType) {
		try {
			Field field = clazz.getField(fieldName); // only finds public fields
			int modifiers = field.getModifiers();

			assertTrue(Modifier.isPublic(modifiers),
					"Field '" + fieldName + "' should be public.");
			assertTrue(Modifier.isStatic(modifiers),
					"Field '" + fieldName + "' should be static.");
			assertEquals(expectedType, field.getType(),
					"Field '" + fieldName + "' should be of type " + expectedType.getSimpleName() + ".");

		} catch (NoSuchFieldException e) {
			fail("Expected public static field '" + fieldName
					+ "' not found in " + clazz.getSimpleName() + ".");
		}
	}

	private void checkPrivateField(Class<?> clazz, String fieldName, Class<?> expectedType) {
		try {
			Field field = clazz.getDeclaredField(fieldName); // finds all declared fields (public, private, etc.)
			int modifiers = field.getModifiers();

			assertTrue(Modifier.isPrivate(modifiers),
					"Field '" + fieldName + "' should be private.");
			assertEquals(expectedType, field.getType(),
					"Field '" + fieldName + "' should be of type " + expectedType.getSimpleName() + ".");

		} catch (NoSuchFieldException e) {
			fail("Expected private field '" + fieldName
					+ "' not found in " + clazz.getSimpleName() + ".");
		}
	}

	private boolean isSubclass(Class<?> childClass, Class<?> parentClass) {
		if (!parentClass.isAssignableFrom(childClass)) {
			System.out.println(childClass.getSimpleName() + " should be a subclass of " + parentClass.getSimpleName());
			return false;
		}
		return true;
	}

	private boolean testMethodNamesForClass(String className, String... expectedMethodNames) throws ClassNotFoundException {
		Class<?> clazz = Class.forName("assignment1." + className);
		Method[] methods = clazz.getDeclaredMethods();
		Map<String, Method> methodMap = new HashMap<>();
		boolean allMethodsExist = true;
		// Add all declared methods to a map for quick lookup
		for (Method method : methods) {
			methodMap.put(method.getName(), method);
		}

		// Check if each expected method name exists and print those that are missing
		for (String methodName : expectedMethodNames) {
			if (!methodMap.containsKey(methodName)) {
				System.out.println("Missing expected method name in class " + className + ": " + methodName);
				allMethodsExist = false;
			}
		}
		return allMethodsExist;
	}

	public static void checkFieldsForInstance(Object instance, Class<?> clasz, Map<String, Map<String, Object>> fields) {
		for (Map.Entry<String, Map<String, Object>> fieldEntry : fields.entrySet()) {
			String fieldName = fieldEntry.getKey();
			Map<String, Object> fieldData = fieldEntry.getValue();
			Field field = null;
			try {
				field = getFieldFromHierarchy(clasz, fieldName);
			} catch (NoSuchFieldException e) {
				fail("Field " + fieldName + " not found in class " + instance.getClass().getName());
			}
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(instance);
			} catch (IllegalAccessException e) {
				fail("Field " + fieldName + " should be accessible.");
			}
			assertEquals(fieldData.get("value"), value, "Field " + fieldName + " should be of type " + fieldData.get("type") + " and have a value of " + fieldData.get("value"));
		}
	}

	private static Field getFieldFromHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				return field;
			} catch (NoSuchFieldException e) {
				// Field not declared in this class; try the superclass
				clazz = clazz.getSuperclass();
			}
		}
		throw new NoSuchFieldException("Field " + fieldName + " not found in class hierarchy.");
	}

	public static void runWithTimeout(Runnable testLogic) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Future<?> future = executor.submit(testLogic);
			future.get(250, MILLISECONDS);
		} catch (TimeoutException e) {
			fail("Test timed out");
		} catch (Exception e) {
			String message = "Test failed with an exception: " + e.getMessage();
			//if (e.getCause() != null && e.getCause().getMessage() != null) {
			//    message += "\n" + e.getCause().getMessage();
			//}
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionDetails = sw.toString();
			if (exceptionDetails.length() > 0) {
				message += "\n" + exceptionDetails;
			}
			fail(message);
		} finally {
			executor.shutdownNow();
		}
	}
	
	public static void runAndAssignCompetency(COMPETENCY competency, Runnable testLogic) {
		runAndAssignMultipleCompetency(competency, 1, testLogic);
	}

	public static void runAndAssignMultipleCompetency(COMPETENCY competency, double amount, Runnable testLogic) {
		totalTests.put(competency, totalTests.get(competency) + amount); // Increment the total number of tests
		runWithTimeout(() -> {
			testLogic.run();
			passedTests.put(competency, passedTests.get(competency) + amount);
		});
	}

	///
	/// Code to run before each test.
	///

	@BeforeEach
	void setUpStaticVariables() throws IllegalAccessException {
		Map<String, Map<String, Object>> staticFields = Map.of(
				"Bladesworn", new HashMap<>() {{
					put("BASE_HEALTH", 100.0);
					put("BASE_COST", 10);
					put("BASE_ATTACK_DAMAGE", 10);
					put("WEAPON_TYPE", 3);
				}},
				"Axebringer", new HashMap<>() {{
					put("BASE_HEALTH", 50.0);
					put("BASE_COST", 50);
					put("BASE_ATTACK_DAMAGE", 5);
					put("WEAPON_TYPE", 2);
				}},
				"Warrior", new HashMap<>() {{
					put("CASTLE_DMG_REDUCTION", 0.5);
				}},
				"Lanceforged", new HashMap<>() {{
					put("BASE_HEALTH", 200.0);
					put("BASE_COST", 200);
					put("BASE_ATTACK_DAMAGE", 20);
					put("WEAPON_TYPE", 1);
				}},
				"Monster", new HashMap<>() {{
					put("BERSERK_THRESHOLD", 10);
				}}
				);

		for (Map.Entry<String, Map<String, Object>> classEntry : staticFields.entrySet()) {
			String className = classEntry.getKey();
			Class<?> clazz = null;
			try {
				clazz = Class.forName("assignment1." + className);
			} catch (ClassNotFoundException e) {
				fail("Class " + className + " not found.");
			}

			Map<String, Object> fieldsMap = classEntry.getValue();
			for (Map.Entry<String, Object> fieldEntry : fieldsMap.entrySet()) {
				String fieldName = fieldEntry.getKey();
				Object value = fieldEntry.getValue();

				Field field = null;
				try {
					field = clazz.getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					fail("Field " + fieldName + " not found in class " + className);
				}
				assertTrue(Modifier.isStatic(field.getModifiers()), "Field " + fieldName + " in class " + className + " should be static.");
				assertTrue(Modifier.isPublic(field.getModifiers()), "Field " + fieldName + " in class " + className + " should be public.");

				// Allow access even if the field is private or protected
				field.setAccessible(true);

				// Set the value. For a static field, the object parameter is ignored (thus, null).
				field.set(null, value);
			}
		}
	}
	
	///
	/// Testing that the expected classes, methods, fields and class relationships exist.
	///
	
	@Test
	@Tag("score:0")
	@Order(1)
	@DisplayName("Test if the expected class names exist")
	void testClassNames() {
		String[] expectedClassNames = {"Axebringer", "Bladesworn", "Fighter", "Lanceforged", "Monster", "MonsterTroop", "Tile", "Warrior"};
		boolean allClassesExist = true;
		for (String className : expectedClassNames) {
			try {
				Class.forName("assignment1." + className);
			} catch (ClassNotFoundException e) {
				System.out.println("Missing class: " + className);
				allClassesExist = false;
			}
		}
		// Print that all classes exist
		assertTrue(allClassesExist, "Not all expected classes exist..");
		System.out.println("All expected class names exist..");
	}

	@Test
	@Tag("score:0")
	@Order(1)
	@DisplayName("Test if Fighter and Warrior are abstract classes")
	void testAbstractClasses() {
		assertTrue(Modifier.isAbstract(Fighter.class.getModifiers()), "Fighter should be an abstract class.");
		assertTrue(Modifier.isAbstract(Warrior.class.getModifiers()), "Warrior should be an abstract class.");
	}

	@Test
	@Tag("score:0")
	@Order(1)
	@DisplayName("Testing inheritance relationships")
	void testInheritance() {
		boolean allInheritanceCorrect = true;
		allInheritanceCorrect &= isSubclass(Bladesworn.class, Warrior.class);
		allInheritanceCorrect &= isSubclass(Axebringer.class, Warrior.class);
		allInheritanceCorrect &= isSubclass(Lanceforged.class, Warrior.class);
		allInheritanceCorrect &= isSubclass(Warrior.class, Fighter.class);
		allInheritanceCorrect &= isSubclass(Monster.class, Fighter.class);
		assertTrue(allInheritanceCorrect, "Not all inheritance relationships are correct. Make sure that the proper classes extend the correct parent classes.");
	}

	@Test
	@Tag("score:0")
	@Order(1)
	@DisplayName("Test if all expected classes have a constructor")
	void testIfAllClassesHaveAConstructor() {
		String[] expectedClassNames = {"Axebringer", "Bladesworn", "Fighter", "Lanceforged", "Monster", "MonsterTroop", "Tile", "Warrior"};
		// Test if all classes have a constructor
		boolean allClassesHaveAConstructor = true;
		for (String className : expectedClassNames) {
			try {
				Constructor<?> constructor = Class.forName("assignment1." + className).getDeclaredConstructors()[0];
				if (constructor.getParameterCount() == 0) {
					if (className.equals("MonsterTroop") || className.equals("Tile")) {
						continue;
					}
					System.out.println("Missing constructor for class: " + className);
					allClassesHaveAConstructor = false;
				}
			} catch (ClassNotFoundException e) {
				System.out.println("Missing class: " + className);
				allClassesHaveAConstructor = false;
			}
		}
		// Print that all classes have a constructor
		assertTrue(allClassesHaveAConstructor, "Not all classes have a constructor.");
	}

	@Test
	@Tag("score:0")
	@Order(1)
	@DisplayName("Test expected method names for each class")
	void testMethodNamesForClasses() throws ClassNotFoundException {
		boolean allMethodsExist = true;
		allMethodsExist &= testMethodNamesForClass("Fighter", "getPosition", "getHealth", "setPosition", "takeDamage", "takeAction", "equals", "getWeaponType", "getAttackDamage");
		allMethodsExist &= testMethodNamesForClass("Monster", "takeDamage", "takeAction", "equals");
		allMethodsExist &= testMethodNamesForClass("Warrior", "getTrainingCost", "takeDamage");
		allMethodsExist &= testMethodNamesForClass("Axebringer", "takeAction");
		allMethodsExist &= testMethodNamesForClass("Bladesworn", "takeAction");
		allMethodsExist &= testMethodNamesForClass("Lanceforged", "takeAction");
		allMethodsExist &= testMethodNamesForClass("Tile", "isCastle", "buildCastle", "isCamp", "buildCamp", "isOnThePath", "towardTheCastle", "towardTheCamp", "createPath", "getNumOfMonsters", "getWarrior", "getMonster", "getMonsters", "addFighter", "removeFighter");
		allMethodsExist &= testMethodNamesForClass("MonsterTroop", "sizeOfTroop", "getMonsters", "getFirstMonster", "addMonster", "removeMonster");

		assertTrue(allMethodsExist, "Not all expected methods exist.");
	}

	@Test
	@Tag("score:4")
	@Order(2)
	@DisplayName("P(4): Check that the appropriate classes have the appropriate public static fields defined.")
	void testPublicAndPrivateFields() {
		runAndAssignMultipleCompetency(COMPETENCY.P, 4, () -> {
			checkPublicStaticField(Axebringer.class, "BASE_HEALTH", double.class);
			checkPublicStaticField(Axebringer.class, "BASE_ATTACK_DAMAGE", int.class);
			checkPublicStaticField(Axebringer.class, "BASE_COST", int.class);
			checkPublicStaticField(Axebringer.class, "WEAPON_TYPE", int.class);

			checkPublicStaticField(Bladesworn.class, "BASE_HEALTH", double.class);
			checkPublicStaticField(Bladesworn.class, "BASE_ATTACK_DAMAGE", int.class);
			checkPublicStaticField(Bladesworn.class, "BASE_COST", int.class);
			checkPublicStaticField(Bladesworn.class, "WEAPON_TYPE", int.class);

			checkPrivateField(Fighter.class, "position", Tile.class);
			checkPrivateField(Fighter.class, "health", double.class);
			checkPrivateField(Fighter.class, "weaponType", int.class);
			checkPrivateField(Fighter.class, "attackDamage", int.class);

			checkPublicStaticField(Lanceforged.class, "BASE_HEALTH", double.class);
			checkPublicStaticField(Lanceforged.class, "BASE_ATTACK_DAMAGE", int.class);
			checkPublicStaticField(Lanceforged.class, "BASE_COST", int.class);
			checkPublicStaticField(Lanceforged.class, "WEAPON_TYPE", int.class);
			checkPrivateField(Lanceforged.class, "piercingPower", int.class);
			checkPrivateField(Lanceforged.class, "actionRange", int.class);

			checkPublicStaticField(Monster.class, "BERSERK_THRESHOLD", int.class);
			checkPrivateField(Monster.class, "rageLevel", int.class);

			checkPrivateField(MonsterTroop.class, "monsters", Monster[].class);
			checkPrivateField(MonsterTroop.class, "numOfMonsters", int.class);

			checkPublicStaticField(Warrior.class, "CASTLE_DMG_REDUCTION", double.class);
			checkPrivateField(Warrior.class, "requiredSkillPoints", int.class);

			checkPrivateField(Tile.class, "isCastle", boolean.class);
			checkPrivateField(Tile.class, "isCamp", boolean.class);
			checkPrivateField(Tile.class, "onThePath", boolean.class);
			checkPrivateField(Tile.class, "towardTheCastle", Tile.class);
			checkPrivateField(Tile.class, "towardTheCamp", Tile.class);
			checkPrivateField(Tile.class, "warrior", Warrior.class);
			checkPrivateField(Tile.class, "troop", MonsterTroop.class);
		});
	}

	///
	/// Fighter tests
	///
	@Test
	@Tag("score:1")
	@Order(5)
	@DisplayName("P: Fighter::setPosition")
	void proficiencyTestFighterSetPosition() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile1 = new Tile() {
				@Override
				public boolean addFighter(Fighter fighter) {
					return true;
				}
			};
			Tile tile2 = new Tile() {
				@Override
				public boolean addFighter(Fighter fighter) {
					return true;
				}
			};
			Fighter fighter = new Fighter(tile1, 100, 1, 10) {
				@Override
				public int takeAction() {
					return 0;
				}
			};
			fighter.setPosition(tile2);
			assertEquals(tile2, fighter.getPosition(), "setPosition should set the position of the fighter to the new tile");
		});
	}

	@Test
	@Tag("score:1")
	@Order(6)
	@DisplayName("P: Fighter::takeAction") // defined abstract
	void proficiencyTestFighterTakeAction() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			try {
				// Retrieve the takeAction method from the Fighter class
				Method takeActionMethod = Fighter.class.getDeclaredMethod("takeAction");
				// Check that it is abstract
				assertTrue(Modifier.isAbstract(takeActionMethod.getModifiers()),
						"takeAction method should be abstract in Fighter class.");
			} catch (NoSuchMethodException e) {
				fail("takeAction method not found in Fighter class.");
			}
		});
	}

	@Test
	@Tag("score:1")
	@Order(9)
	@DisplayName("P: Fighter::takeDamage") // reduces health
	void proficiencyTestFighterTakeDamage() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile = new Tile() {
				@Override
				public boolean addFighter(Fighter fighter) {
					return true;
				}
			};
			Fighter fighter = new Fighter(tile, 100, 1, 10) {
				@Override
				public int takeAction() {
					return 0;
				}
			};
			fighter.takeDamage(10, 1);
			assertEquals(90, fighter.getHealth(), "takeDamage should reduce the health of the fighter.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(12)
	@DisplayName("P: Fighter::takeDamage") // returns the correct damage dealt
	void proficiencyTestFighterTakeDamageReturn() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile = new Tile() {
				@Override
				public boolean addFighter(Fighter fighter) {
					return true;
				}
			};
			Fighter fighter = new Fighter(tile, 100, 1, 10) {
				@Override
				public int takeAction() {
					return 0;
				}
			};
			assertEquals(10, fighter.takeDamage(10, 1), "takeDamage should return the damage dealt.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(14)
	@DisplayName("AM: Fighter::equals") // check that two Fighters with the same type, position and health are equal
	void approachingMasteryTestFighterEquals() {
		runAndAssignCompetency(COMPETENCY.AM, () -> {
			Tile tile = new Tile() {
				@Override
				public boolean addFighter(Fighter fighter) {
					return true;
				}
			};

			// Define a factory that creates instances of the same anonymous Fighter subclass.
			Supplier<Fighter> fighterFactory = new Supplier<>() {
				@Override
				public Fighter get() {
					return new Fighter(tile, 100, 1, 10) {
						@Override
						public int takeAction() {
							return 0;
						}
					};
				}
			};

			Fighter fighter1 = fighterFactory.get();
			Fighter fighter2 = fighterFactory.get();
			assertTrue(fighter1.equals(fighter2), "Two Fighters with the same type, position and health should be equal.");
		});
	}

	///
	/// Monster tests
	///



	@Test
	@Tag("score:1")
	@Order(17)
	@DisplayName("AM: Monster::takeAction") // attacks if a warrior is on the tile, and deals the correct amount of damage
	void approachingMasteryTestMonsterTakeActionAttack() {
		runAndAssignCompetency(COMPETENCY.AM, () -> {
			Tile tile = createTileOnPath();
			Monster monster = new Monster(tile, 100, 1, 10);
			Warrior warrior = new Warrior(tile, 100, 1, 10, 10) {
				@Override
				public int takeAction() {
					return 0;
				}
			};
			monster.takeAction();
			assertEquals(90, warrior.getHealth(), "takeAction should attack the warrior on the tile.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(19)
	@DisplayName("M: Monster::takeAction") // moves the monster to the next tile towards the castle if there is no warrior on the tile
	void masteryTestMonsterTakeActionMoveTile() {
		runAndAssignCompetency(COMPETENCY.M, () -> {
			Tile tile = createTileOnPath();
			Tile tile2 = createTileOnPath();
			Tile tile3 = createTileOnPath();
			Monster monster = new Monster(tile, 100, 1, 10);
			tile.createPath(tile2, tile3);
			monster.takeAction();
			assertNull(tile.getMonster(), "takeAction should move the monster to the next tile if there is no warrior on the tile.");
			assertEquals(monster, tile2.getMonster(), "takeAction should move the monster to the next tile if there is no warrior on the tile.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(21)
	@DisplayName("AM: Monster::takeDamage") // increases the rage level appropriately
	void approachingMasteryTestMonsterTakeDamageRage() {
		runAndAssignCompetency(COMPETENCY.AM, () -> {
			Tile tile = createTileOnPath();
			Monster monster = new Monster(tile, 100, 1, 10);
			monster.takeDamage(10, 2);
			Field rageLevelField = null;
			try {
				rageLevelField = Monster.class.getDeclaredField("rageLevel");
			} catch (NoSuchFieldException e) {
				fail("rageLevel field not found in Monster class.");
			}
			rageLevelField.setAccessible(true);
			try {
				assertEquals(1, rageLevelField.getInt(monster), "takeDamage should increase the rage level appropriately.");
			} catch (IllegalAccessException e) {
				fail("rageLevel field should be accessible.");
			}
		});
	}

	@Test
	@Tag("score:1")
	@Order(24)
	@DisplayName("P: Monster::equals") // correctly compares the attack damage of two Monsters
	void proficiencyTestMonsterEqualsAttackDamage() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile = createTileOnPath();
			Monster monster1 = new Monster(tile, 100, 1, 10);
			Monster monster2 = new Monster(tile, 100, 1, 11);
			assertFalse(monster1.equals(monster2), "Monsters with different attack damage should not be equal.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(27)
	@DisplayName("P: Warrior::getTrainingCost")
	void proficiencyTestWarriorGetTrainingCost() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile = new Tile();
			Warrior warrior = new Warrior(tile, 100, 1, 10, 10) {
				@Override
				public int takeAction() {
					return 0;
				}
			};
			assertEquals(10, warrior.getTrainingCost(), "getTrainingCost should return the required skill points.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(29)
	@DisplayName("AM: Warrior::takeDamage") // does not apply the castle damage reduction when not on a castle tile
	void approachingMasteryTestWarriorTakeDamageNoCastle() {
		runAndAssignCompetency(COMPETENCY.AM, () -> {
			Tile tile = new Tile();
			Warrior warrior = new Warrior(tile, 100, 1, 10, 10) {
				@Override
				public int takeAction() {
					return 0;
				}
			};
			warrior.takeDamage(10, 1);
			assertEquals(90, warrior.getHealth(), "takeDamage should not apply the castle damage reduction when not on a castle tile.");
		});
	}

	///
	/// Bladesworn
	///

	@Test
	@Tag("score:1")
	@Order(30)
	@DisplayName("P: Bladesworn constructor")
	void proficiencyTestBladeswornConstructor() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile = new Tile();
			Bladesworn bladesworn = new Bladesworn(tile) {
				@Override
				public int takeAction() {
					return 0;
				}
			};

			// check if the fields are set correctly
			Map<String, Map<String, Object>> fields = Map.of(
					"health", Map.of("type", double.class, "value", 100.0),
					"weaponType", Map.of("type", int.class, "value", 3),
					"attackDamage", Map.of("type", int.class, "value", 10),
					"position", Map.of("type", Tile.class, "value", tile),
					"requiredSkillPoints", Map.of("type", int.class, "value", 10)
					);
			checkFieldsForInstance(bladesworn, Bladesworn.class, fields);
		});
	}

	@Test
	@Tag("score:1")
	@Order(31)
	@DisplayName("AM: Bladesworn::takeAction") // will attack if a monster is present on the same tile
	void approachingMasteryTestBladeswornTakeActionAttack() {
		runAndAssignCompetency(COMPETENCY.AM, () -> {
			Tile tile = createTileOnPath();
			Bladesworn bladesworn = new Bladesworn(tile);
			Monster monster = new Monster(tile, 100, 2, 10);
			tile.addFighter(monster);
			bladesworn.takeAction();
			assertEquals(85, monster.getHealth(), "takeAction should attack the monster on the tile.");
		});
	}
	
	@Test
	@Order(34)
	@DisplayName("M: Bladesworn::takeAction") // will not move to the next tile if the next tile is a camp, even if there is no monster on the current or next tile
	void masteryTestBladeswornTakeActionNoMoveCamp() {
		runAndAssignCompetency(MiniTest.COMPETENCY.M, () -> {
			Tile tile = new Tile();
			Tile tile2 = new Tile();
			Bladesworn bladesworn = new Bladesworn(tile);
			tile.createPath(tile2, new Tile(false, true, false, null, null, null, null));
			bladesworn.takeAction();
			assertEquals(bladesworn, tile.getWarrior(), "takeAction should not move the Bladesworn to the next tile if the next tile is a camp.");
		});
	}
	
	@Test
	@Tag("score:1")
	@Order(35)
	@DisplayName("AM: Bladesworn::takeAction") // returns the correct number of skill points
	void approachingMasteryTestBladeswornTakeActionSkillPoints() {
		runAndAssignCompetency(COMPETENCY.AM, () -> {
			Tile tile = createTileOnPath();
			Bladesworn bladesworn = new Bladesworn(tile);
			Monster monster = new Monster(tile, 100, 1, 10);
			tile.addFighter(monster);
			int skillPoints = bladesworn.takeAction();
			int expectedDamageDealt = 15;
			int expectedSkillPoints = (int) Math.floor(bladesworn.getAttackDamage()/expectedDamageDealt + 1);
			assertEquals(expectedSkillPoints, skillPoints, "takeAction should return the required skill points.");
		});
	}

	///
	/// Axebringer
	///

	@Test
	@Tag("score:1")
	@Order(36)
	@DisplayName("P: Axebringer constructor")
	void proficiencyTestAxebringerConstructor() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile = new Tile();
			Axebringer axebringer = new Axebringer(tile) {
				@Override
				public int takeAction() {
					return 0;
				}
			};

			// check if the fields are set correctly
			Map<String, Map<String, Object>> fields = Map.of(
					"health", Map.of("type", double.class, "value", 50.0),
					"weaponType", Map.of("type", int.class, "value", 2),
					"attackDamage", Map.of("type", int.class, "value", 5),
					"position", Map.of("type", Tile.class, "value", tile),
					"requiredSkillPoints", Map.of("type", int.class, "value", 50)
					);
			checkFieldsForInstance(axebringer, Axebringer.class, fields);
		});
	}

	@Test
	@Tag("score:1")
	@Order(37)
	@DisplayName("AM: Axebringer::takeAction") // will attack if a monster is present on the same tile
	void approachingMasteryTestAxebringerTakeActionAttack() {
		runAndAssignCompetency(COMPETENCY.AM, () -> {
			Tile tile = createTileOnPath();
			Axebringer axebringer = new Axebringer(tile);
			Monster monster = new Monster(tile, 100, 2, 10);
			tile.addFighter(monster);
			axebringer.takeAction();
			assertEquals(95, monster.getHealth(), "takeAction should attack the monster on the tile.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(38)
	@DisplayName("P: Axebringer::takeAction") //  will only attack the monster on its tile even if there is also a monster on the next tile
	void proficiencyTestAxebringerTakeActionAttackOneMonster() {
		runAndAssignMultipleCompetency(COMPETENCY.P, 0.5, () -> {
			Tile tile = createTileOnPath();
			Tile tile2 = createTileOnPath();
			Axebringer axebringer = new Axebringer(tile);
			Monster monster = new Monster(tile, 100, 2, 10);
			Monster monster2 = new Monster(tile2, 100, 2, 10);
			tile.addFighter(monster);
			tile2.addFighter(monster2);
			tile.createPath(new Tile(), tile2);
			axebringer.takeAction();
			assertEquals(95, monster.getHealth(), "takeAction should attack the monster on the tile.");
			assertEquals(100, monster2.getHealth(), "takeAction should not attack the monster on the next tile.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(43)
	@DisplayName("P: Axebringer::takeAction") //  attacks the monster on the next tile, its own position is unchanged.
	void proficiencyTestAxebringerTakeActionMove() {
		System.out.println("Unit test???");
		runAndAssignMultipleCompetency(COMPETENCY.P, 0.5, () -> {
			Tile tile = createTileOnPath();
			System.out.println("908");
			Tile tile2 = createTileOnPath();
			System.out.println("910");
			Axebringer axebringer = new Axebringer(tile);
			Monster monster = new Monster(tile2, 100, 2, 10);
			tile.createPath(new Tile(), tile2);
			axebringer.takeAction();
			assertEquals(axebringer, tile.getWarrior(), "takeAction should not move the Axebringer to the next tile after attacking the monster.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(44)
	@DisplayName("AM: Axebringer::takeAction") // returns the correct number of skill points
	void approachingMasteryTestAxebringerTakeActionSkillPoints() {
		runAndAssignCompetency(COMPETENCY.AM, () -> {
			Tile tile = createTileOnPath();
			Axebringer axebringer = new Axebringer(tile);
			Monster monster = new Monster(tile, 100, 1, 10);
			tile.addFighter(monster);
			int skillPoints = axebringer.takeAction();
			double expectedDamageDealt = 7.5;
			int expectedSkillPoints = (int) Math.floor(axebringer.getAttackDamage()/expectedDamageDealt + 1);
			assertEquals(expectedSkillPoints, skillPoints, "takeAction should return the required skill points.");
		});
	}

	///
	/// MonsterTroop
	///

	@Test
	@Tag("score:1")
	@Order(47)
	@DisplayName("P: MonsterTroop constructor") // in particular that the size field is initialized to zero
	void proficiencyTestMonsterTroopConstructor() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			MonsterTroop troop = new MonsterTroop();
			Field sizeField = null;
			try {
				sizeField = MonsterTroop.class.getDeclaredField("numOfMonsters");
			} catch (NoSuchFieldException e) {
				fail("numOfMonsters field not found in MonsterTroop class.");
			}
			sizeField.setAccessible(true);
			try {
				assertEquals(0, sizeField.getInt(troop), "The numOfMonsters field should be initialized to zero.");
			} catch (IllegalAccessException e) {
				fail("numOfMonsters field should be accessible.");
			}
		});
	}

	@Test
	@Tag("score:1")
	@Order(48)
	@DisplayName("P: MonsterTroop::sizeOfTroop")
	void proficiencyTestMonsterSizeOfTroop() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			// Create an empty MonsterTroop instance.
			MonsterTroop troop = new MonsterTroop();

			// Create an array of 3 dummy Monster objects.
			// (You can use simple instances; the details aren’t important for testing sizeOfTroop.)
			Monster[] dummyMonsters = new Monster[3];
			for (int i = 0; i < 3; i++) {
				// Create a dummy Tile as needed.
				Tile dummyTile = createTileOnPath();
				dummyMonsters[i] = new Monster(dummyTile, 100, 1, 10);
			}

			// Use reflection to set the private field 'monsters' (if needed) and 'numOfMonsters'.
			Field monstersField = null;
			try {
				monstersField = MonsterTroop.class.getDeclaredField("monsters");
			} catch (NoSuchFieldException e) {
				fail("monsters field not found in MonsterTroop class.");
			}
			monstersField.setAccessible(true);
			try {
				monstersField.set(troop, dummyMonsters);
			} catch (IllegalAccessException e) {
				fail("monsters field should be accessible.");
			}

			Field numOfMonstersField = null;
			try {
				numOfMonstersField = MonsterTroop.class.getDeclaredField("numOfMonsters");
			} catch (NoSuchFieldException e) {
				fail("numOfMonsters field not found in MonsterTroop class.");
			}
			numOfMonstersField.setAccessible(true);
			try {
				numOfMonstersField.setInt(troop, dummyMonsters.length);
			} catch (IllegalAccessException e) {
				fail("numOfMonsters field should be accessible.");
			}

			assertEquals(3, troop.sizeOfTroop(), "sizeOfTroop should return the number of monsters in the troop.");
		});
	}


	@Test
	@Tag("score:1")
	@Order(51)
	@DisplayName("P: MonsterTroop::getFirstMonster") // returns the first monster in the troop if there is only one monster
	void proficiencyTestMonsterGetFirstMonsterOne() {
		runAndAssignMultipleCompetency(COMPETENCY.P, 0.5, () -> {
			// --- ORIGINAL TEST (Array Injection) ---
			MonsterTroop troop = new MonsterTroop();
			Monster[] dummyMonsters = new Monster[1];
			Tile dummyTile = createTileOnPath();
			Monster m1 = new Monster(dummyTile, 100, 1, 10);
			dummyMonsters[0] = m1;
			
			// Inject into private array
			try {
				Field monstersField = MonsterTroop.class.getDeclaredField("monsters");
				monstersField.setAccessible(true);
				monstersField.set(troop, dummyMonsters);
				
				Field numOfMonstersField = MonsterTroop.class.getDeclaredField("numOfMonsters");
				numOfMonstersField.setAccessible(true);
				numOfMonstersField.setInt(troop, 1);
				
				// Try assertion #1
				assertEquals(m1, troop.getFirstMonster(), "getFirstMonster should return the first monster (Array check).");
				
			} catch (org.opentest4j.AssertionFailedError e) {
				// --- FALLBACK (Linked List Compatibility) ---
				// If they failed the array check, let's try using their own addMonster method
				// This allows Linked List implementations to pass if they work internally.
				MonsterTroop fallbackTroop = new MonsterTroop();
				Tile t = createTileOnPath();
				Monster m = new Monster(t, 100, 1, 10);
				fallbackTroop.addMonster(m);
				
				assertEquals(m, fallbackTroop.getFirstMonster(),
						"getFirstMonster failed with array injection, and also failed when using addMonster().");
			} catch (Exception e) {
				fail("Reflection failed or unexpected error: " + e.getMessage());
			}
		});
	}

	@Test
	@Tag("score:1")
	@Order(52)
	@DisplayName("P: MonsterTroop::getFirstMonster") // returns the first monster in the troop if there are multiple monsters
	void proficiencyTestMonsterGetFirstMonsterTwo() {
		runAndAssignMultipleCompetency(COMPETENCY.P, 0.5, () -> {
			MonsterTroop troop = new MonsterTroop();
			Monster[] dummyMonsters = new Monster[2];
			Tile dummyTile = createTileOnPath();
			Monster m1 = new Monster(dummyTile, 100, 1, 10);
			Monster m2 = new Monster(dummyTile, 100, 2, 10);
			dummyMonsters[0] = m1;
			dummyMonsters[1] = m2;
			
			try {
				// Inject into private array
				Field monstersField = MonsterTroop.class.getDeclaredField("monsters");
				monstersField.setAccessible(true);
				monstersField.set(troop, dummyMonsters);
				
				Field numOfMonstersField = MonsterTroop.class.getDeclaredField("numOfMonsters");
				numOfMonstersField.setAccessible(true);
				numOfMonstersField.setInt(troop, 2);
				
				// Try assertion #1
				assertEquals(m1, troop.getFirstMonster(), "getFirstMonster should return the first monster.");
				
			} catch (org.opentest4j.AssertionFailedError e) {
				// --- FALLBACK ---
				MonsterTroop fallbackTroop = new MonsterTroop();
				Tile t = createTileOnPath();
				Monster fm1 = new Monster(t, 100, 1, 10);
				Monster fm2 = new Monster(t, 100, 2, 10);
				
				fallbackTroop.addMonster(fm1);
				fallbackTroop.addMonster(fm2);
				
				assertEquals(fm1, fallbackTroop.getFirstMonster(),
						"getFirstMonster failed with array injection, and also failed when using addMonster().");
			} catch (Exception e) {
				fail("Reflection failed: " + e.getMessage());
			}
		});
	}

	@Test
	@Tag("score:1")
	@Order(56)
	@DisplayName("P: MonsterTroop::addMonster") // check that numOfMonsters field is correctly updated.
	void proficiencyTestMonsterAddMonster() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			MonsterTroop troop = new MonsterTroop();
			Tile dummyTile = createTileOnPath();
			Monster m1 = new Monster(dummyTile, 100, 1, 10);
			
			troop.addMonster(m1);
			
			try {
				// --- ORIGINAL CHECK (Inspect Private Array) ---
				Field monstersField = MonsterTroop.class.getDeclaredField("monsters");
				monstersField.setAccessible(true);
				Monster[] internalArray = (Monster[]) monstersField.get(troop);
				
				// Check if it is in the array
				assertEquals(m1, internalArray[0], "Monster was not found in the private monsters array.");
				
				Field numOfMonstersField = MonsterTroop.class.getDeclaredField("numOfMonsters");
				numOfMonstersField.setAccessible(true);
				int num = numOfMonstersField.getInt(troop);
				assertEquals(1, num, "numOfMonsters was not updated.");
				
			} catch (org.opentest4j.AssertionFailedError e) {
				// --- FALLBACK (Check via Public API) ---
				// If they didn't put it in the array, did they put it somewhere reachable via getMonsters()?
				Monster[] publicArray = troop.getMonsters();
				
				assertNotNull(publicArray, "getMonsters() returned null.");
				assertTrue(publicArray.length >= 1, "getMonsters() returned empty array after addMonster.");
				assertEquals(m1, publicArray[0],
						"Monster not found in private array, AND not found in getMonsters() return.");
				
				// If we are here, they passed via the Fallback!
			} catch (Exception e) {
				fail("Reflection failed: " + e.getMessage());
			}
		});
	}
	
	
	
	@Test
	@Tag("score:1")
	@Order(58)
	@DisplayName("P: MonsterTroop::removeMonster") // check that we return false if the monster is not present and the troop is not empty
	void proficiencyTestMonsterRemoveMonsterNotPresent() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			// Create an empty MonsterTroop instance.
			MonsterTroop troop = new MonsterTroop();

			// Create an array of 10 dummy Monster objects.
			Monster[] monsters = new Monster[10];
			for (int i = 0; i < 10; i++) {
				// Create a dummy Tile as needed.
				Tile dummyTile = createTileOnPath();
				monsters[i] = new Monster(dummyTile, 100, 1, 10);
				troop.addMonster(monsters[i]);
			}

			// Create a dummy Monster object.
			Monster dummyMonster = new Monster(createTileOnPath(), 100, 1, 10);

			// call removeMonster
			assertFalse(troop.removeMonster(dummyMonster), "removeMonster should return false if the monster is not present in the troop.");
		});
	}
	
	@Test
	@Tag("score:1")
	@Order(62)
	@DisplayName("P: MonsterTroop::removeMonster") // check that after removing one monster from a troop of ten, the numOfMonsters field decreases by 1.
	void proficiencyTestMonsterRemoveMonsterNumOfMonsters() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			// Create an empty MonsterTroop instance.
			MonsterTroop troop = new MonsterTroop();

			Monster[] dummyMonsters = new Monster[10];
			for (int i = 0; i < 10; i++) {
				dummyMonsters[i] = new Monster(createTileOnPath(), 100, 1, 10);
				troop.addMonster(dummyMonsters[i]);
			}

			// call removeMonster
			troop.removeMonster(dummyMonsters[9]);

			Field numOfMonstersField = null;
			try {
				numOfMonstersField = MonsterTroop.class.getDeclaredField("numOfMonsters");
			} catch (NoSuchFieldException e) {
				fail("numOfMonsters field not found in MonsterTroop class.");
			}
			numOfMonstersField.setAccessible(true);
			try {
				assertEquals(9, numOfMonstersField.getInt(troop), "numOfMonsters field should be updated correctly.");
			} catch (IllegalAccessException e) {
				fail("numOfMonsters field should be accessible.");
			}
		});
	}

	@Test
	@Tag("score:1")
	@Order(64)
	@DisplayName("AM: MonsterTroop::getMonsters") // returns a list of ten monsters in the correct order
	void approachingMasteryTestMonsterGetMonsters() {
		runAndAssignMultipleCompetency(COMPETENCY.AM, 1.0, () -> {
			MonsterTroop troop = new MonsterTroop();
			Monster[] dummyMonsters = new Monster[3];
			Tile dummyTile = createTileOnPath();
			
			Monster m1 = new Monster(dummyTile, 100, 1, 10);
			Monster m2 = new Monster(dummyTile, 100, 2, 10);
			Monster m3 = new Monster(dummyTile, 100, 3, 10);
			
			dummyMonsters[0] = m1;
			dummyMonsters[1] = m2;
			dummyMonsters[2] = m3;
			
			try {
				// Inject into private array
				Field monstersField = MonsterTroop.class.getDeclaredField("monsters");
				monstersField.setAccessible(true);
				monstersField.set(troop, dummyMonsters);
				
				Field numOfMonstersField = MonsterTroop.class.getDeclaredField("numOfMonsters");
				numOfMonstersField.setAccessible(true);
				numOfMonstersField.setInt(troop, 3);
				
				Monster[] result = troop.getMonsters();
				assertNotNull(result, "getMonsters returned null");
				assertEquals(3, result.length, "getMonsters returned wrong length (Array Check).");
				assertEquals(m1, result[0]);
				assertEquals(m2, result[1]);
				assertEquals(m3, result[2]);
				
			} catch (org.opentest4j.AssertionFailedError e) {
				// --- FALLBACK ---
				// Try adding via public method
				MonsterTroop fallbackTroop = new MonsterTroop();
				Tile t = createTileOnPath();
				Monster fm1 = new Monster(t, 100, 1, 10);
				Monster fm2 = new Monster(t, 100, 2, 10);
				Monster fm3 = new Monster(t, 100, 3, 10);
				
				fallbackTroop.addMonster(fm1);
				fallbackTroop.addMonster(fm2);
				fallbackTroop.addMonster(fm3);
				
				Monster[] result = fallbackTroop.getMonsters();
				assertNotNull(result, "getMonsters returned null");
				assertEquals(3, result.length, "getMonsters returned wrong length (Fallback Check).");
				assertEquals(fm1, result[0]);
				assertEquals(fm2, result[1]);
				assertEquals(fm3, result[2]);
			} catch (Exception e) {
				fail("Reflection failed: " + e.getMessage());
			}
		});
	}

	///
	/// Tile
	///

	@Test
	@Tag("score:1")
	@Order(71)
	@DisplayName("P: Tile constructor") // with no arguments
	void proficiencyTestTileConstructor() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile = new Tile();
			Map<String, Map<String, Object>> fields = new HashMap<>();
			fields.put("isCastle", new HashMap<>(Map.of("type", boolean.class, "value", false)));
			fields.put("isCamp", new HashMap<>(Map.of("type", boolean.class, "value", false)));
			fields.put("onThePath", new HashMap<>(Map.of("type", boolean.class, "value", false)));

			// For entries with null values, create the map manually:
			Map<String, Object> warriorField = new HashMap<>();
			warriorField.put("type", Warrior.class);
			warriorField.put("value", null);
			fields.put("warrior", warriorField);

			Map<String, Object> castleField = new HashMap<>();
			castleField.put("type", Tile.class);
			castleField.put("value", null);
			fields.put("towardTheCastle", castleField);

			Map<String, Object> campField = new HashMap<>();
			campField.put("type", Tile.class);
			campField.put("value", null);
			fields.put("towardTheCamp", campField);

			checkFieldsForInstance(tile, Tile.class, fields);

			// check monster troop
			assertDoesNotThrow(() -> tile.getMonsters(), "A Tile constructed with no arguments should not throw an exception when getMonsters is called.");
			assertDoesNotThrow(() -> tile.getNumOfMonsters(), "A Tile constructed with no arguments should not throw an exception when getNumOfMonsters is called.");
			assertEquals(0, tile.getNumOfMonsters(), "A Tile constructed with no arguments should have a MonsterTroop with 0 monsters.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(72)
	@DisplayName("P: Tile constructor") // with all arguments
	void proficiencyTestTileConstructorAllArgs() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile1 = new Tile();
			Tile tile2 = new Tile();
			Warrior warrior = new Warrior(tile1, 100, 1, 10, 10) {
				@Override
				public int takeAction() {
					return 0;
				}
			};
			MonsterTroop monsterTroop = new MonsterTroop();
			Tile tile = new Tile(true, true, true, tile1, tile2, warrior, monsterTroop);
			Map<String, Map<String, Object>> fields = Map.of(
					"isCastle", Map.of("type", boolean.class, "value", true),
					"isCamp", Map.of("type", boolean.class, "value", true),
					"warrior", Map.of("type", Warrior.class, "value", warrior),
					"troop", Map.of("type", MonsterTroop.class, "value", monsterTroop),
					"onThePath", Map.of("type", boolean.class, "value", true),
					"towardTheCastle", Map.of("type", Tile.class, "value", tile1),
					"towardTheCamp", Map.of("type", Tile.class, "value", tile2)
					);

			checkFieldsForInstance(tile, Tile.class, fields);
		});
	}

	@Test
	@Tag("score:1")
	@Order(73)
	@DisplayName("P: Tile getter methods")
	void proficiencyTestTileGetters() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile1 = new Tile();
			Tile tile2 = new Tile();
			Warrior warrior = new Warrior(tile1, 100, 1, 10, 10) {
				@Override
				public int takeAction() {
					return 0;
				}
			};
			MonsterTroop monsterTroop = new MonsterTroop();
			Tile tile = new Tile(false, false, true, tile1, tile2, warrior, monsterTroop);
			assertEquals(false, tile.isCastle(), "isCastle should return the correct value.");
			assertEquals(false, tile.isCamp(), "isCamp should return the correct value.");
			assertEquals(warrior, tile.getWarrior(), "getWarrior should return the correct value.");
			assertArrayEquals(monsterTroop.getMonsters(), tile.getMonsters(), "getMonsterTroop should return the correct value.");
			assertEquals(true, tile.isOnThePath(), "isOnThePath should return the correct value.");
			assertEquals(tile1, tile.towardTheCastle(), "towardTheCastle should return the correct value.");
			assertEquals(tile2, tile.towardTheCamp(), "towardTheCamp should return the correct value.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(78)
	@DisplayName("P: Tile::addFighter") // with a Warrior passed as argument
	void proficiencyTestTileAddFighterWarrior() {
		runAndAssignCompetency(COMPETENCY.P, () -> {
			Tile tile = createTileOnPath();
			Tile tile2 = createTileOnPath();
			Warrior warrior = new Warrior(tile, 100, 1, 10, 10) {
				@Override
				public int takeAction() {
					return 0;
				}
			};
			tile2.addFighter(warrior);
			assertEquals(warrior, tile2.getWarrior(), "addFighter should add a Warrior to the tile.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(79)
	@DisplayName("P: Tile::addFighter") // with a Monster passed as argument
	void proficiencyTestTileAddFighterMonster() {
		runAndAssignMultipleCompetency(COMPETENCY.P, 0.5, () -> {
			Tile tile = createTileOnPath();
			Tile tile2 = createTileOnPath();
			Monster monster = new Monster(tile, 100, 1, 10);
			tile2.addFighter(monster);
			assertEquals(monster, tile2.getMonsters()[0], "addFighter should add a Monster to the tile.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(87)
	@DisplayName("P: Tile::removeFighter") // can remove its warrior if passed the same one
	void proficiencyTestTileRemoveFighterWarrior() {
		runAndAssignMultipleCompetency(COMPETENCY.P, 0.5, () -> {
			Tile tile = createTileOnPath();
			Warrior warrior = new Warrior(tile, 100, 1, 10, 10) {
				@Override
				public int takeAction() {
					return 0;
				}
			};
			tile.removeFighter(warrior);
			assertNull(tile.getWarrior(), "removeFighter should remove the Warrior from the tile.");
		});
	}

	@Test
	@Tag("score:1")
	@Order(88)
	@DisplayName("P: Tile::removeFighter") // can remove the given monster from its troop
	void proficiencyTestTileRemoveFighterMonster() {
		runAndAssignMultipleCompetency(COMPETENCY.P, 0.5, () -> {
			Tile tile = createTileOnPath();
			Monster monster = new Monster(tile, 100, 1, 10);
			tile.removeFighter(monster);
			assertEquals(0, tile.getNumOfMonsters(), "removeFighter should remove the Monster from the tile.");
		});
	}
	
	@Test
	@Order(98)
	@DisplayName("AM: Lanceforged::takeAction") //  will attack if a troop is on the same tile
	void masteryTestLanceforgedTakeActionSameTile() {
		runAndAssignCompetency(MiniTest.COMPETENCY.AM, () -> {
			Tile tile = createTileOnPath();
			Lanceforged lanceforged = new Lanceforged(tile, 100, 1);
			Monster monster = new Monster(tile, 100, 1, 10);
			lanceforged.takeAction();
			
			// check if monster took damage
			assertEquals(80, monster.getHealth(), "The Lanceforged should attack the Monster if it is on the same tile.");
		});
	}

}
