package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.zip.CheckedInputStream;

public class CountingBloomFilter {
    Random random;

    private int nElementsInserted, nElementsRemoved, nOtherElementsAdded, nFilterSize, nHashes;
    private int[] filter;
    private int[] hashes;
    //inputs: elementsInserted, elementsRemoved, numElementsAdded, filterSize, numHashes

    public CountingBloomFilter(int nElementsInserted, int nElementsRemoved, int nOtherElementsAdded,
                               int nFilterSize, int nHashes){
        this.nElementsInserted = nElementsInserted;
        this.nElementsRemoved = nElementsRemoved;
        this.nOtherElementsAdded = nOtherElementsAdded;
        this.nFilterSize = nFilterSize;
        this.nHashes = nHashes;

        initializeFilter();
    }

    private void initializeFilter() {
        random = new Random();
        hashes = random.ints( 1, Integer.MAX_VALUE).distinct().limit(nHashes).toArray();
        filter = new int[nFilterSize];
    }


    private int getIndex(int element, int index){
        return (hashes[index] ^ element)%nFilterSize;
    }




    public int[] generateSet(int size){
        return random.ints( 1, Integer.MAX_VALUE).distinct().limit(size).toArray();
    }


    public void insertSimulation(int[] set){
        for(int e : set){
            for(int i = 0; i < hashes.length; i++){
                filter[getIndex(e,i)]++;
            }
        }
    }

    public boolean lookup(int e){
        for(int i = 0; i < hashes.length; i++){
            if(filter[getIndex(e,i)] == 0){
                return false;
            }
        }
        return true;
    }

    //checking if element is present and then deleting it
    public void delete(int e){
        if(lookup(e)){
            for(int i = 0; i < hashes.length; i++){
                filter[getIndex(e,i)]--;
            }
        }

    }

    public void simulate(){
        int[] setA = generateSet(nElementsInserted);

        insertSimulation(setA);
        //removing
        for(int i = 0; i < nElementsRemoved; i++){
            delete(setA[i]);
        }
        //insert another
        int[] setB = generateSet(nOtherElementsAdded);
        insertSimulation(setB);

        //check elements of A
        int count = 0;
        for(int e : setA){
            if(lookup(e)){
                count++;
            }
        }

        System.out.println("Number of elements found in setA:" + count);




    }

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please enter number of elements to be encoded initially");
            int nElementsInserted = Integer.parseInt(reader.readLine());

            System.out.println("Please enter number of elements to be removed");
            int nElementsRemoved = Integer.parseInt(reader.readLine());

            System.out.println("Please enter number of extra elements to be added");
            int nOtherElementsAdded = Integer.parseInt(reader.readLine());

            System.out.println("Please number of counter in the filter");
            int nFilterSize = Integer.parseInt(reader.readLine());

            System.out.println("Please number of Hashes");
            int nHashes = Integer.parseInt(reader.readLine());

            CountingBloomFilter bloomFilter = new CountingBloomFilter(nElementsInserted,nElementsRemoved,
                            nOtherElementsAdded,nFilterSize,nHashes);
            bloomFilter.simulate();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //public void deleteAndLookUp{}


}
