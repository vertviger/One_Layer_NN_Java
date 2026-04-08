import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class NAI_MPP3_s32785
{
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

        public LinkedHashMap<String, String> readDataFromFiles()
        {
            LinkedHashMap<String, String> result = new LinkedHashMap<>();
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
        public ArrayList<LanguageVector> languagesWithRates = new ArrayList<>();

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
                languagesWithRates.add(new LanguageVector(key));
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
            for(Character key : alphabet)
            {
                result.add(toOrganize.get(key)/textSize);
            }
            return result;
        }

        public ArrayList<LanguageVector> fillOutLanguages()
        {
            for(int i = 0; i < languagesWithRates.size(); i++)
            {
                languagesWithRates.get(i).cords = produceCharacterOccurancesList(produceCharacterNumberMap(languagesWithRates.get(i).Text), languagesWithRates.get(i).Text.length()); 
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
    static class LanguageVector
    {
        public ArrayList<Double> cords = new ArrayList<>();
        public int label;
        //language part
        public String name;
        private String Text;
        private HashMap<Character, Double> characterRates;
        private ArrayList<Character> alphabet;
        //language part
        
        public LanguageVector(){}
        public LanguageVector(String name) 
        {
            this.name = name;
            this.alphabet = new ArrayList<>();
            for (char c = 'a'; c <= 'z'; c++) 
            {
                alphabet.add(c);
            }
        }
        public LanguageVector(ArrayList<Double> cords, int label)
        {
            this.cords = cords;
            this.label = label;
        }
        public LanguageVector(LanguageVector other)
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
        public double distance(LanguageVector vec2)
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
        public LanguageVector sum(LanguageVector vec2)
        {
            LanguageVector result = new LanguageVector();
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

        public LanguageVector scalar(double factor)
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
            return new LanguageVector(result, this.label);
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append("\n");
            for(int i = 0; i < cords.size(); i++)
            {
                sb.append(alphabet.get(i) + ": " + cords.get(i) + "\n");
            }
            return sb.toString();
        }
    }
///////////////////////////////////////////////////PERCEPTRON////////////////////////////////////////////////////////////////////////////////
    static class Perceptron
    {
        int dimension = 0;
        //private ArrayList<LanguageVector> inputs;
        private ArrayList<Double> weights;
        private double treshold;
        private double alpha;
        private double beta;
        public Perceptron(/*ArrayList<LanguageVector> inputs, */ArrayList<Double> weights, double treshold, double alpha, double beta)
        {
            //this.inputs = inputs;
            this.weights = weights;
            this.treshold = treshold;
            this.alpha = alpha;
            this.beta = beta;
            this.dimension = weights.size();
        }
        public Perceptron(Perceptron other)
        {
            //this.inputs = other.inputs;
            this.weights = other.weights;
            this.treshold = other.treshold;
            this.alpha = other.alpha;
            this.beta = other.beta;
        }
        public HashMap<Integer, ArrayList<LanguageVector>> train(ArrayList<LanguageVector> inputs, ArrayList<LanguageVector> correctAnswers)
        {
            int maxNunberOfEpochs = 10000;
            boolean trainingCompleted = false;
            HashMap<Integer, ArrayList<LanguageVector>> results= new HashMap<>();
            int numberOfEpochs = 0;
            while(!trainingCompleted && maxNunberOfEpochs-- > 0)
            {
                numberOfEpochs++;
                results.put(numberOfEpochs, new ArrayList<LanguageVector>());
                trainingCompleted = true;
                for(int i = 0; i < inputs.size(); i++)
                {
                    results.get(numberOfEpochs).add(new LanguageVector(inputs.get(i).cords, predict(inputs.get(i))));
                    if(correctAnswers.get(i).getType() != predict(inputs.get(i)))
                    {
                        trainingCompleted = false;
                        // w = w + (d - y)*x*alpha
                        double sign = (correctAnswers.get(i).getType() - predict(inputs.get(i)));
                        LanguageVector weightsVector = new LanguageVector(this.weights, 0);
                        LanguageVector toAdd = inputs.get(i).scalar(sign*this.alpha);
                        weightsVector = weightsVector.sum(toAdd);
                        this.weights = weightsVector.cords;
                        //treshold = treshold - (d - y)*beta
                        this.treshold -= sign*this.beta;
                    }
                }
            }
            return results;
        }
 
        public int predict(LanguageVector input)
        {
            return (input.dot_product(this.weights) - this.treshold > 0) ? 1 : 0;
        }

        public double predictDouble(LanguageVector input)
        {
            return input.dot_product(this.weights) - this.treshold; 
        }
        public ArrayList<LanguageVector> predictOnDataset(ArrayList<LanguageVector> inputs)
        {
            ArrayList<LanguageVector> predictions = new ArrayList<>();
            for(int i = 0; i < inputs.size(); i++)
            {
                predictions.add(new LanguageVector(inputs.get(i).getCords(), predict(inputs.get(i))));
            }
            return predictions;
        }
    }
    static class SingleLayerNeuralNetwork
    {
        Double alpha;
        Double beta;
        Double threshold;
        ArrayList<Double> weights = new ArrayList<>();
        ArrayList<Perceptron> neurons = new ArrayList<>();
        ArrayList<LanguageVector> languages = new ArrayList<>();
        public SingleLayerNeuralNetwork(Double alpha, Double beta, ArrayList<LanguageVector> languages)
        {
            Random rand = new Random();
            this.threshold = rand.nextDouble();
            this.alpha = alpha;
            this.beta = beta;
            for(int i = 0; i < 26; i++) weights.add(rand.nextDouble());
            this.languages = languages;
            for(int i = 0; i < languages.size(); i++) {languages.get(i).label = 0;}
            neurons.add(new Perceptron(new ArrayList<>(this.weights), this.threshold, this.alpha, this.beta));
            neurons.add(new Perceptron(new ArrayList<>(this.weights), this.threshold, this.alpha, this.beta));
            neurons.add(new Perceptron(new ArrayList<>(this.weights), this.threshold, this.alpha, this.beta));
        }
        public void trainLayer()
        {
            for(int i = 0; i < neurons.size(); i++)
            {
                ArrayList<LanguageVector> specificLanguage = new ArrayList<>(languages);
                specificLanguage.get(i).label = 1;
                this.neurons.get(i).train(languages, specificLanguage);
                specificLanguage.get(i).label = 0;
            }
        }
        public int predict(LanguageVector toPredict)
        {
            int predictedLanguageIndex = -1;
            Double prediction0 = Double.NEGATIVE_INFINITY;
            for(int i = 0; i < neurons.size(); i++) 
            {
                Double prediction = this.neurons.get(i).predictDouble(toPredict);
                if(prediction > prediction0) 
                {
                    prediction0 = prediction;
                    predictedLanguageIndex = i;
                }
            }
            return predictedLanguageIndex;
        }
    }
    public static void main(String[] args) 
    {
        System.out.println("Hi");
        ArrayList<String> trainingFileParths = new ArrayList<>();
        trainingFileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TrainTextEnglish.txt");
        trainingFileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TrainTextFrench.txt");
        trainingFileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TrainTextGerman.txt");
        

        DataReader reader = new DataReader(trainingFileParths);
        HashMap<String, String> readingResult = reader.readDataFromFiles();
        
        DataDivider divider = new DataDivider(readingResult);
        divider.divideData();
        ArrayList<LanguageVector> resultingLanguageDatas = divider.fillOutLanguages();
        for(int i = 0; i < resultingLanguageDatas.size(); i++)
        {
            System.out.println(resultingLanguageDatas.get(i));
        }
        
        // for(int i = 0; i < resultingLanguageDatas.size(); i++)
        // {
        //     resultingLanguageDatas.get(i).label = 0;
        // }

        SingleLayerNeuralNetwork test = new SingleLayerNeuralNetwork(0.1, 0.1, resultingLanguageDatas);
        test.trainLayer();
        
        System.out.println("Testing");
        ArrayList<String> testingFileParths = new ArrayList<>();
        testingFileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TestTextEnglish.txt");
        testingFileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TestTextFrench.txt");
        testingFileParths.add("E:\\!Programs\\NAI_Solutions\\MPP3\\TestTextGerman.txt");

        DataReader readerTest = new DataReader(testingFileParths);
        HashMap<String, String> readingResultTest = readerTest.readDataFromFiles();
        
        DataDivider dividerTest = new DataDivider(readingResultTest);
        dividerTest.divideData();
        ArrayList<LanguageVector> resultingLanguageDatasTest = dividerTest.fillOutLanguages();

        int toPredict = (int)(Math.random()*3);
        int predictedIndexTest = test.predict(resultingLanguageDatasTest.get(toPredict));

        System.out.println("Real language " + resultingLanguageDatasTest.get(toPredict).name + " | Predicted language: " + resultingLanguageDatasTest.get(predictedIndexTest).name);
    }
}
