import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class NAI_MPP3_s32785
{
///////////////////////////////////////////////////LANGUAGE_WRAPPER////////////////////////////////////////////////////////////////////////////////
    static class LanguageData
    {
        public String name;
        public String Text;
        public HashMap<Character, Double> characterRates;
        public ArrayList<Double> rates;
        public ArrayList<Character> alphabet;
        public LanguageData(String name) 
        {
            this.name = name;
            this.alphabet = new ArrayList<>();
            for (char c = 'a'; c <= 'z'; c++) 
            {
                alphabet.add(c);
            }
        }
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append("\n");
            for(int i = 0; i < rates.size(); i++)
            {
                sb.append(alphabet.get(i) + ": " + rates.get(i) + "\n");
            }
            return sb.toString();
        }
    }
///////////////////////////////////////////////////DATA_READING////////////////////////////////////////////////////////////////////////////////
    static class DataReader
    {
        private ArrayList<String> filePaths = new ArrayList<>();

        public DataReader(ArrayList<String> filePaths)
        {
            for(String path : filePaths)
            {
                this.filePaths.add(path);
            }
        }

        public HashMap<String, String> readDataFromFiles()
        {
            HashMap<String, String> result = new HashMap<>();
            for(String path : filePaths)
            {
                try
                {
                    File file = new File(path);

                    String rawFileContent = readFileContent(path);
                    String cleanedFileContent = rawFileContent.toLowerCase().replaceAll("[^a-z]", "");
                    result.put(file.getName(), cleanedFileContent);
                }
                catch(Exception e)
                {
                    System.out.println("Problem with file reading");
                    e.printStackTrace();
                }
            }
            return result;
        }

        public static String readFileContent(String filePath) throws IOException 
        {
            return Files.readString(Paths.get(filePath));
        }
    }
///////////////////////////////////////////////////DATA_PROCESSING////////////////////////////////////////////////////////////////////////////////
    static class DataDivider
    {
        private HashMap<String, String> initialData;
        private ArrayList<String> trainingStrings = new ArrayList<>();
        private ArrayList<String> testingStrings = new ArrayList<>();
        private ArrayList<Character> alphabet;
        public ArrayList<LanguageData> languagesWithRates = new ArrayList<>();

        public DataDivider(HashMap<String, String> initialStrings) 
        {
            this.initialData = initialStrings;
            this.alphabet = new ArrayList<>();
            for (char c = 'a'; c <= 'z'; c++) 
            {
                alphabet.add(c);
            }
        }

        public void divideData()
        {
            ArrayList<String> initialStrings = new ArrayList<>(initialData.values());
            for(String key : initialData.keySet())
            {
                languagesWithRates.add(new LanguageData(key));
            }
            for(int i = 0; i < initialStrings.size(); i++)
            {
                languagesWithRates.get(i).Text = initialStrings.get(i);
            }
            
            // int languagesNumber = (initialStrings.size()/2);
            // for(int i = 0; i < languagesNumber; i++) { trainingStrings.add(initialStrings.get(i)); }
            // for(int i = languagesNumber; i < initialStrings.size(); i++) { testingStrings.add(initialStrings.get(i)); }
        }

        public HashMap<Character, Double> produceCharacterNumberMap(String input)
        {
            HashMap<Character, Double> result = new HashMap<>();
            for(int i = 0; i < alphabet.size(); i++)
            {
                result.put(alphabet.get(i), 0.0);
            }
            char[] chars = input.toCharArray();
            for(int i = 0; i < chars.length; i++)
            {
                Double oldValue = result.get(chars[i]);
                result.put(chars[i], ++oldValue);
            }
            return result;
        }

        public ArrayList<Double> produceCharacterOccurancesList(HashMap<Character, Double> toOrganize, Integer textSize)
        {
            ArrayList<Double> result = new ArrayList<>();
            for(Character key : toOrganize.keySet())
            {
                result.add(toOrganize.get(key)/textSize);
            }
            return result;
        }

        public ArrayList<LanguageData> fillOutLanguages()
        {
            for(int i = 0; i < languagesWithRates.size(); i++)
            {
                languagesWithRates.get(i).rates = produceCharacterOccurancesList(produceCharacterNumberMap(languagesWithRates.get(i).Text), languagesWithRates.get(i).Text.length()); 
            }
            return this.languagesWithRates;
        }

        public void printData()
        {
            System.out.println("Training data");
            int number = 1;
            for(String str : trainingStrings)
            {
                System.out.println(number++ + " " + str);
            }
            System.out.println("Testing data");
            number = 1;
            for(String str : testingStrings)
            {
                System.out.println(number++ + " " + str);
            }
        }

        public HashMap<String, HashMap<Character, Integer>> divideTestingData()
        {
            HashMap<String, HashMap<Character, Integer>> result = new HashMap<>();
            return result;
        }
    }
///////////////////////////////////////////////////MY_VECTOR////////////////////////////////////////////////////////////////////////////////
    static class MyVector
    {
        private ArrayList<Double> cords = new ArrayList<>();
        private int label;
        public MyVector(){}
        public MyVector(ArrayList<Double> cords, int label)
        {
            this.cords = cords;
            this.label = label;
        }
        public MyVector(MyVector other)
        {
            this.label= other.label;
            this.cords = other.cords;
        }
        public ArrayList getCords() {return cords;}
        public int getType() {return label;}
        public double dot_product(ArrayList<Double> vec2)
        {
            double result = 0;
            if(this.cords.size() != vec2.size())
            {
                System.out.println("Can't work on vectors of different sizes");
                return result;   
            }
            for(int i = 0; i < this.cords.size(); i++)
            {
                result += this.cords.get(i) * vec2.get(i);
            } 
            //System.out.println(result);
            return result;
        }
        public double distance(MyVector vec2)
        {
            double result = 0;
            if(this.cords.size() != vec2.cords.size())
            {
                System.out.println("Can't work on vectors of different sizes");
                return result;    
            }
            for(int i = 0; i < this.cords.size(); i++)
            {
                result += Math.pow(this.cords.get(i) - vec2.cords.get(i), 2);
            }
            result = Math.sqrt(result); 
            return result;
        }
        public MyVector sum(MyVector vec2)
        {
            MyVector result = new MyVector();
            if(this.cords.size() != vec2.cords.size())
            {
                System.out.println("Can't add vectors of different sizes");
                return result;    
            }
            for(int i = 0; i < this.cords.size(); i++)
            {
                result.cords.add(this.cords.get(i) + vec2.cords.get(i));
            }  
            // for(Double i : result.cords)
            // {
            //     System.out.print(i + " ");
            // }
            // System.out.println();
            return result;  
        }

        public MyVector scalar(double factor)
        {
            ArrayList<Double> result = new ArrayList<>();
            for(int i = 0; i < this.cords.size(); i++)
            {
                result.add(this.cords.get(i) * factor);
            }
            // for(Double i : result)
            // {
            //     System.out.print(i + " ");
            // }
            // System.out.println();
            return new MyVector(result, this.label);
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Vector type: " + this.label + " | Coordinates: ");
            for(Double cord : cords)
            {
                sb.append(" " + cord + " ");
            }
            return sb.toString();
        }
    }
///////////////////////////////////////////////////PERCEPTRON////////////////////////////////////////////////////////////////////////////////
    static class Perceptron
    {
        int dimension = 0;
        private ArrayList<MyVector> inputs;
        private ArrayList<Double> weights;
        private double treshold;
        private double alpha;
        private double beta;
        public Perceptron(ArrayList<MyVector> inputs, ArrayList<Double> weights, double treshold, double alpha, double beta)
        {
            this.inputs = inputs;
            this.weights = weights;
            this.treshold = treshold;
            this.alpha = alpha;
            this.beta = beta;
            this.dimension = weights.size();
        }
        public Perceptron(Perceptron other)
        {
            this.inputs = other.inputs;
            this.weights = other.weights;
            this.treshold = other.treshold;
            this.alpha = other.alpha;
            this.beta = other.beta;
        }
        public HashMap<Integer, ArrayList<MyVector>> train(ArrayList<MyVector> inputs, ArrayList<MyVector> correctAnswers)
        {
            int maxNunberOfEpochs = 10000;
            boolean trainingCompleted = false;
            HashMap<Integer, ArrayList<MyVector>> results= new HashMap<>();
            int numberOfEpochs = 0;
            while(!trainingCompleted && maxNunberOfEpochs-- > 0)
            {
                numberOfEpochs++;
                results.put(numberOfEpochs, new ArrayList<MyVector>());
                trainingCompleted = true;
                for(int i = 0; i < inputs.size(); i++)
                {
                    results.get(numberOfEpochs).add(new MyVector(inputs.get(i).cords, predict(inputs.get(i))));
                    if(correctAnswers.get(i).getType() != predict(inputs.get(i)))
                    {
                        trainingCompleted = false;
                        // w = w + (d - y)*x*alpha
                        double sign = (correctAnswers.get(i).getType() - predict(inputs.get(i)));
                        MyVector weightsVector = new MyVector(this.weights, 0);
                        MyVector toAdd = inputs.get(i).scalar(sign*this.alpha);
                        weightsVector = weightsVector.sum(toAdd);
                        this.weights = weightsVector.cords;
                        //treshold = treshold - (d - y)*beta
                        this.treshold -= sign*this.beta;
                    }
                }
            }
            return results;
        }
 
        public int predict(MyVector input)
        {
            return (input.dot_product(this.weights) - this.treshold > 0) ? 1 : 0;
        }
        public ArrayList<MyVector> predictOnDataset(ArrayList<MyVector> inputs)
        {
            ArrayList<MyVector> predictions = new ArrayList<>();
            for(int i = 0; i < inputs.size(); i++)
            {
                predictions.add(new MyVector(inputs.get(i).getCords(), predict(inputs.get(i))));
            }
            return predictions;
        }
    }
    public static void main(String[] args) 
    {
        System.out.println("Hi");
        ArrayList<String> fileParths = new ArrayList<>();
        fileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TrainTextEnglish.txt");
        fileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TestTextEnglish.txt");
        fileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TrainTextGerman.txt");
        fileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TestTextGerman.txt");
        fileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TrainTextFrench.txt");
        fileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TestTextFrench.txt");

        DataReader reader = new DataReader(fileParths);
        HashMap<String, String> readingResult = reader.readDataFromFiles();
        
        DataDivider divider = new DataDivider(readingResult);
        divider.divideData();
        ArrayList<LanguageData> resultingLanguageDatas = divider.fillOutLanguages();
        for(int i = 0; i < resultingLanguageDatas.size(); i++)
        {
            System.out.println(resultingLanguageDatas.get(i));
        }
    }
}
