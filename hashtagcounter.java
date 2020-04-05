import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class hashtagcounter {

    private static boolean ArgumentsNotValid(String[] args) {
        return false;
    }

    public static void main(String[] args) throws IOException {

        if(args.length < 1 || args.length > 2) {
            System.out.println("Invalid number of arguments" +
                    "\nExample of valid format:" +
                    "\n$hashtagcounter input_file_name output_file_name" +
                    "\nOR" +
                    "\n$hashtagcounter input_file_name");
        }
        else{
            if (!ArgumentsNotValid(args)) {
                String outputFileName;
                String inputFileName = args[0];
                if(args.length == 2){
                    outputFileName = args[1];
                }
                else{
                    outputFileName = "";
                }

                hashtagcounter myHashTagCounter = new hashtagcounter();
                myHashTagCounter.run(inputFileName, outputFileName);
            }
        }
    }

    private void run(String inputFileName, String outputFileName) throws IOException {
        String currentDir = System.getProperty("user.dir");
        //Read contents of Input file
        System.out.println("File Path: "+ currentDir + "//" + inputFileName);                 // Handle for windows and linus and mac the slash
        File myFile = new File(currentDir + "//" + inputFileName);
        if(!myFile.exists()){
            System.out.println("File does not exist");
            System.exit(0);
        }
        FileInputStream myFileStream = new FileInputStream(myFile);
        InputStreamReader myInput = new InputStreamReader(myFileStream);
        BufferedReader myBR = new BufferedReader(myInput);
        String currentLine;

        Map<String, Node<HashTags>> hashTagMap = new HashMap<>();
        FibonacciHeaps<HashTags> heap = new FibonacciHeaps<>(new HashtagComparator());

        while((currentLine = myBR.readLine()) != null){
            String hashTagName = "";
            int hashTagFrequency = 0;

            String[] tempArrOfStrings = currentLine.split(" ");

            if(tempArrOfStrings.length == 2){
                hashTagName = tempArrOfStrings[0];
                hashTagFrequency = Integer.parseInt(tempArrOfStrings[1]);
            }
            else if(tempArrOfStrings.length == 1){

                if(tempArrOfStrings[0].equals("STOP")){
                    System.out.println("Exiting program...");
                    System.exit(0);
                }
                else{
                   // System.out.println("String 0: "+ tempArrOfStrings[0]);
                    int removeMaxAmount = Integer.parseInt(tempArrOfStrings[0]);
                    System.out.println("Print commented");
                    //PrintOutput(removeMaxAmount,hashTagMap,heap);
                }

            }
            else{
                System.out.println("Error: Bad Line!!");
            }

            HashTags newHashTag = new HashTags(hashTagName,hashTagFrequency);

            if(hashTagMap.containsKey(hashTagName)){
                //increase frequency
                newHashTag.frequency += hashTagMap.get(hashTagName).value.frequency;
                heap.decreaseKey(hashTagMap.get(hashTagName), newHashTag);
            }
            else{
                //Insert
//                HashTags newHashTag = new HashTags(hashTagName,hashTagFrequency);
                Node<HashTags> hashTagNode = heap.insert(newHashTag);
                hashTagMap.put(hashTagName, hashTagNode);
            }

        }
    }

    private void PrintOutput(int removeMaxAmount, Map<String,Node<HashTags>> hashTagMap, FibonacciHeaps<HashTags> heap) {
        System.out.println("Printing output");
        Map<String, Integer> hm = new HashMap<>();
        for (int i = 0; i < removeMaxAmount; i++) {
            //Remove Max
            HashTags tempRemove = heap.removeMin();
            //Store tags in Buffer
            hm.put(tempRemove.name,tempRemove.frequency);
            //Display with comma
            System.out.println(tempRemove.name + ",");
        }
        System.out.println("\n");
        //Insert the tags back into heap from buffer
        for (Map.Entry mapElement : hm.entrySet()) {
            String hashTagName = (String)mapElement.getKey();
            int hashTagFrequency = (int)mapElement.getValue();
            HashTags newHashTag = new HashTags(hashTagName,hashTagFrequency);
            Node<HashTags> hashTagNode = heap.insert(newHashTag);
            hashTagMap.put(hashTagName, hashTagNode);
        }

        //Refresh the hm
        hm.clear();

    }


}




