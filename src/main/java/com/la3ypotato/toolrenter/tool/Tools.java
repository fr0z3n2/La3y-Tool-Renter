package com.la3ypotato.toolrenter.tool;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class serves as a singleton instance of all the tools available for rent that are loaded from the CSV file. This
 * class is designed to be a singleton instance because there is added risk when reading from a file multiple times, so
 * to reduce that risk the file is only loaded once when this instance is created initially.
 *
 * @author Logan Stanfield
 * @version 1.0
 * @since 02/23/2022
 */
public class Tools {
    // Singleton class instance
    private static Tools toolsInstance;
    // Class properties
    private final int EXPECTED_TOOL_ARGS = 7;
    private final String FILE_DELIMITER= ",";
    private String resourceCSVFile = "/tools.csv";
    private Map<String, Tool> availableTools;

    /**
     * When this singleton instance is initialized begin to load the tools from the CSV file.
     */
    private Tools() {
        // Load the tools when this singleton instance is initialized.
        availableTools = loadTools();
    }

    /**
     * This method returns the currents Tools instance if one is already created. If the instance is null, one is
     * created. This will load the rentable tools from the CSV file.
     *
     * @return - Current Tools instance.
     */
    public static Tools getInstance() {
        if (toolsInstance == null) {
            toolsInstance = new Tools();
        }

        return toolsInstance;
    }

    /**
     * This method when invoked will begin to read the resource CSV file and attempt to build the availableTools Map
     * instance. Note, the tool creation will fail to be created if there is insufficient data in the resource file.
     *
     * Note, duplicate tool codes will result in overriding the existing tool in the Map.
     *
     * @return - A Map<String,Tool> instance containing all of the data for the available rental tools.
     */
    private Map<String, Tool> loadTools() {
        Map<String, Tool> retToolMap = new HashMap<>();
        try {
            InputStream in = getClass().getResourceAsStream(resourceCSVFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = "";
            // Read each line of the CSV resource. Each line can be a created tool.
            while ( (line = br.readLine()) != null ) {
                String[] toolInfo = line.split(FILE_DELIMITER);
                Tool createdTool = createTool(toolInfo);
                retToolMap.put(createdTool.toolCode, createdTool);
            }
            // Closing the BufferedReader.
            br.close();
        // createTool can throw an IllegalArgumentException if an incorrect
        // number of fields defined are in the CSV file.
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return retToolMap;
    }

    /**
     * This method attempts to create a tool instance based on the array that is passed. This method is expecting the
     * tool details in the following order of the String[] in order of position.
     *
     * 0 - toolCode
     * 1 - toolType
     * 2 - brand
     * 3 - daily charge
     * 4 - weekday charge
     * 5 - weekend charge
     * 6 - holiday charge
     *
     * @param toolInfo - passed String[] array containing the desired info for the tool to be created. Note, the passed
     *                   information needs to be in the order above.
     * @return - the generated tool based on the param data.
     * @throws IllegalArgumentException - thrown if the toolsInfo array contains insufficient number of fields.
     */
    public Tool createTool(String ...toolInfo) throws IllegalArgumentException {
        // Prevent insufficient data from being provided in the CSV file. This expecting to
        // have each
        if (toolInfo.length != EXPECTED_TOOL_ARGS) {
            throw new IllegalArgumentException("Incorrect number of tool parameters!\n" +
                                               "Expected: " + EXPECTED_TOOL_ARGS + "\n" +
                                               "Received: " + toolInfo.length + "\n" +
                                               "Problematic tool line: " + Arrays.toString(toolInfo));
        }
        // De-construct the toolInfo array.
        String toolCode = toolInfo[0];
        String toolType = toolInfo[1];
        String brand = toolInfo[2];
        String dailyCharge = toolInfo[3];
        String weekdayCharge = toolInfo[4];
        String weekendCharge = toolInfo[5];
        String holidayCharge = toolInfo[6];
        // Defining the tool.
        Tool tool = new Tool(toolCode, toolType, brand);
        // Convert the read in string to a double.
        tool.dailyCharge = Double.valueOf(dailyCharge);
        // Convert the 'yes'/'no' strings to a primitive boolean.
        tool.weekdayCharge = weekdayCharge.toLowerCase().equals("yes") ? true : false;
        tool.weekendCharge = weekendCharge.toLowerCase().equals("yes") ? true : false;
        tool.holidayCharge = holidayCharge.toLowerCase().equals("yes") ? true : false;
        return tool;
    }

    /**
     * This method obtains the current availableTools Map<String, Tool> where the toolCode is the identifier is the key
     * and the tool directly co
     *
     * @return - the availableTools Map<String, Tool> instance.
     */
    public Map<String, Tool> getAvailableTools() {
        return availableTools;
    }
}
