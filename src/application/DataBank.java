package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBank {
    static public Program currentlyEditProgram;
    static private HashMap<String, Program> programs = new HashMap<String, Program>();
    static private HashMap<String, HashMap<String, Object>> programVariables = new HashMap<String, HashMap<String, Object>>();
    static private HashMap<String, HashMap<String, Object>> programInstances = new HashMap<String, HashMap<String, Object>>();
    static private MySQLConnection mySQLInstance;

    static public List<String> getProgramNames() {
        List<String> nameList = new ArrayList<String>();

        for (Program program : programs.values()) {
            nameList.add(program.getProgramName());
        }

        return nameList;
    }

    static public void renameProgram(Program program, String name) {
        programs.remove(program.getProgramName());
        program.setProgramName(name);
        programs.put(name, program);
    }

    static public Program getProgramByName(String name) {
        return programs.get(name);
    }

    private static void addProgram(Program program) {
        programs.put(program.getProgramName(), program);
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

    public static Object loadInstanceObject(String referenceID, String name) {
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
                mySQLInstance = MySQLConnection.getInstance();
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
                mySQLInstance = MySQLConnection.getInstance();
            }

            PreparedStatement preparedStatement = mySQLInstance.getPreparedStatement("update node set contained_text = ?, source = ? where id = ?");
            if (preparedStatement != null) {
                preparedStatement.setString(1, node.getContainedText());
                preparedStatement.setString(2, node.getSource().getSource());
                preparedStatement.setInt(3, node.getId());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadPrograms() {
        try {
            if (mySQLInstance == null) {
                mySQLInstance = MySQLConnection.getInstance();
            }

            ResultSet resultSet = mySQLInstance.runQuery("select id,name from program;");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Integer programId = resultSet.getInt("id");
                Program loadedProgram = new Program(name, programId);
                ResultSet sourceResultSet = mySQLInstance.runQuery("select id,source,contained_text,referenceID from node where program_id = '" + programId + "';");

                while (sourceResultSet.next()) {
                    loadedProgram.getFlowController().createNewNode(
                            sourceResultSet.getInt("id"),
                            sourceResultSet.getString("contained_text"),
                            sourceResultSet.getString("source"),
                            sourceResultSet.getString("referenceID"),
                            true);
                }

                addProgram(loadedProgram);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}