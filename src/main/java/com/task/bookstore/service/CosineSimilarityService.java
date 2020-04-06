package com.task.bookstore.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CosineSimilarityService {


    Double getSimilarityScore(List<Integer> selectedBookVector, List<Integer> suggestedBookVector) {
        double dotProd = calculateDotProduct(selectedBookVector, suggestedBookVector);

        double selectedVectorMagnitude = calculateVectorMagnitude(selectedBookVector);
        double suggestedVectorMagnitude = calculateVectorMagnitude(suggestedBookVector);


        return (dotProd / (selectedVectorMagnitude + suggestedVectorMagnitude));
    }

    private double calculateVectorMagnitude(List<Integer> selectedBookVector) {
        double magnitude = 0d;
        for (Integer val : selectedBookVector) {
            magnitude += Math.pow(val, 2);
        }
        return Math.sqrt(magnitude);
    }

    private double calculateDotProduct(List<Integer> selectedBookVector, List<Integer> suggestedBookVector) {
        double dotProd = 0d;
        for (int i = 0; i < selectedBookVector.size(); i++) {
            dotProd += selectedBookVector.get(i) * suggestedBookVector.get(i);
        }
        return dotProd;
    }
}
