package application.utils;

import application.FlowNode;
import application.MySQLConnectionManager;
import application.Program;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBank {
    static public Program currentlyEditProgram;
    static private HashMap<Integer, Program> programs = new HashMap<Integer, Program>();
    static private HashMap<String, HashMap<String, Object>> programVariables = new HashMap<String, HashMap<String, Object>>();
    static private HashMap<String, HashMap<String, Object>> programInstances = new HashMap<String, HashMap<String, Object>>();
    static private MySQLConnectionManager mySQLInstance;

    static public List<String> getProgramNames() {
        List<String> nameList = new ArrayList<String>();

        for (Program program : programs.values()) {
            nameList.add(program.getProgramName());
        }

        return nameList;
    }

//    static public void renameProgram(Program program, String name) {
//        programs.remove(program.getProgramName());
//        program.setProgramName(name);
//        programs.put(name, program);
//    }

    public static Program getProgramById(Integer id) {
        return programs.get(id);
    }

    private static void addProgram(Program program) {
        programs.put(program.getId(), program);
    }

    static public List<Program> getPrograms() {
        return new ArrayList(programs.values());
    }

    public static void saveVariable(String name, Object object, String referenceID) {
        HashMap<String, Object> programVariable = programVariables.get(referenceID);
        if (programVariable == null) {
            programVariable = new HashMap<String, Object>();
        }
        programVariable.put(name, object);
        programVariables.put(referenceID, programVariable);
    }

    public static Object loadVariable(String name, String referenceID) {
        HashMap<String, Object> programVariable = programVariables.get(referenceID);
        if (programVariable != null) {
            return programVariable.get(name);
        }
        return null;
    }

    public static void resetInstanceObject(String referenceID) {
        programInstances.remove(referenceID);
    }

    public static void saveInstanceObject(String referenceID, String name, Object instance) {
        HashMap<String, Object> instances = programInstances.get(referenceID);
        if (instances == null) {
            instances = new HashMap<String, Object>();
        }

        instances.put(name, instance);
        programInstances.put(referenceID, instances);
    }

    public static Object getInstanceObject(String referenceID, String name) {
        HashMap<String, Object> instances = programInstances.get(referenceID);
        if (instances != null) {
            return instances.get(name);
        }

        return null;
    }

    public static void loadFromDatabase() {
        loadPrograms();
    }

    public static Program createNewProgram(String programName) {
        Program newProgram = new Program(programName);
        addProgram(newProgram);
        try {
            if (mySQLInstance == null) {
                mySQLInstance = MySQLConnectionManager.getInstance();
            }

            PreparedStatement preparedStatement = mySQLInstance.getPreparedStatement("insert into program values (default, ?)");
            if (preparedStatement != null) {
                preparedStatement.setString(1, programName);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newProgram;
    }

    public static void saveNode(FlowNode node) {
        try {
            if (mySQLInstance == null) {
                mySQLInstance = MySQLConnectionManager.getInstance();
            }

            PreparedStatement preparedStatement = mySQLInstance.getPreparedStatement("update node set contained_text = ?, source = ?, source_x = ?, source_y = ? where id = ?");
            if (preparedStatement != null) {
                preparedStatement.setString(1, node.getContainedText());
                preparedStatement.setString(2, node.getSource().getSource());
                preparedStatement.setDouble(3, node.getX());
                preparedStatement.setDouble(4, node.getY());
                preparedStatement.setInt(5, node.getId());
                int result = preparedStatement.executeUpdate();
                preparedStatement.close();

                if (result == 0) { // If record does not exist insert a new one..
                    node.setId(getNextId("node")); // Gets the next ID for a node that is about to be created

                    preparedStatement = mySQLInstance.getPreparedStatement("insert into node values (default, ?, ?, ?, ?, ?, ?)");
                    if (preparedStatement != null) {
                        preparedStatement.setInt(1, node.getProgramId());
                        preparedStatement.setString(2, node.getContainedText());
                        preparedStatement.setString(3, node.getSource().getSource());
                        preparedStatement.setInt(4, node.getId());
                        preparedStatement.setDouble(5, node.getX());
                        preparedStatement.setDouble(6, node.getY());
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadPrograms() {
        try {
            if (mySQLInstance == null) {
                mySQLInstance = MySQLConnectionManager.getInstance();
            }

            ResultSet resultSet = mySQLInstance.runQuery("select id,name from program;");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Integer programId = resultSet.getInt("id");
                Program loadedProgram = new Program(name, programId);
                ResultSet sourceResultSet = mySQLInstance.runQuery("select id,program_id,source,contained_text,reference_id,source_x,source_y from node where program_id = '" + programId + "';");

                while (sourceResultSet.next()) {
                    loadedProgram.getFlowController().createNewNode(
                            sourceResultSet.getInt("id"),
                            sourceResultSet.getInt("program_id"),
                            sourceResultSet.getString("contained_text"),
                            sourceResultSet.getString("source"),
                            sourceResultSet.getString("reference_id"),
                            sourceResultSet.getDouble("source_x"),
                            sourceResultSet.getDouble("source_y"),
                            true);
                }

                addProgram(loadedProgram);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Integer getNextId(String tableName) {
        Integer autoIncrement = -1;
        try {
            if (mySQLInstance == null) {
                mySQLInstance = MySQLConnectionManager.getInstance();
            }

            ResultSet resultSet = mySQLInstance.runQuery("SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = '" + tableName + "' AND table_schema = DATABASE();");
            while (resultSet.next()) {
                autoIncrement = resultSet.getInt("AUTO_INCREMENT");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autoIncrement;
    }
}