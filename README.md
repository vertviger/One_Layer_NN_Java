# Language Detection Perceptron (Java)
A simple Java application implementing a single-layer neural network (perceptron) to classify and detect languages (English, French, and German) based on character frequency vectors extracted from text files.

## Features
**Data Preprocessing**: Reads text files, cleans text to lowercase alphabetical characters only, and converts texts into normalized character occurrence vectors (frequency distribution across the 26 letters of the alphabet).
**Single-Layer Neural Network**: Trains multiple perceptrons to distinguish between languages using custom learning rates and epoch training limits. 
**Evaluation Metrics**: Evaluates the network's performance against test datasets, automatically calculating Accuracy, Precision, Recall, and F-Measure per language.

## Setup and Launch
**Java Development Kit**: Java 11 or higher 
**Dataset Files**: Ensure your training and testing text files exist at the specified file paths
