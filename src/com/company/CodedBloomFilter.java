package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CodedBloomFilter {
    //inputs: numSets, numElementsInSet, numFilters, filterSize, nHashes
    private int nSets, nSetElements, nFilter, nFilterNumElements, nHashes;
    private int[][] sets;
    private int[][] filters;
    private int[] hashes;
    Random random;
    //HashMap<Integer, Integer> elementSetMap;
    public CodedBloomFilter(int nSets, int nSetElements, int nFilter, int nFilterNumElements, int nHashes){
        this.nSets = nSets;
        this.nSetElements = nSetElements;
        this.nFilter = nFilter;

        this.nFilterNumElements = nFilterNumElements;
        //changing for zero
        this.nFilter++;
        this.nSets++;
        this.nHashes = nHashes;
        initializeFilter();

    }

    private void initializeFilter() {
        random = new Random();
        hashes = random.ints( 1, Integer.MAX_VALUE).distinct().limit(nHashes).toArray();
        filters = new int[nFilter][nFilterNumElements];
        sets = new int[nSets][];
        //elementSetMap = new HashMap<>();

    }

    public List<Integer> filterSelector(int setIndex){
        String s = Integer.toBinaryString(setIndex);
        List<Integer> selectedFilters = new ArrayList<>();
        int start = nFilter - 1;
        for(int i = s.length() - 1; i >= 0; i--){
            if(s.charAt(i) =='1'){
                selectedFilters.add(start);
            }
            start--;
        }
        return selectedFilters;
    }

    public boolean checkIfExistsInFilter(int e, int filterIndex){
        for(int i = 0; i < hashes.length; i++){
            if(filters[filterIndex][getIndex(e,i)] == 0){
                return false;
            }
        }
        return true;
    }

    public int decodeElementToSetNumber(int element){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < nFilter; i++){
            if(checkIfExistsInFilter(element,i)){
                stringBuilder.append("1");
            }else{
                stringBuilder.append("0");
            }
        }
       // System.out.println(stringBuilder.toString() +" converted to "+ Integer.parseInt(stringBuilder.toString(), 2));
        return Integer.parseInt(stringBuilder.toString(), 2);

    }


    private int getIndex(int element, int index){
        return (hashes[index] ^ element)% nFilterNumElements;
    }


    public int[] generateSet(int size){
        return random.ints( 1, Integer.MAX_VALUE).distinct().limit(size).toArray();
    }

    public void insertInFilter(int[] set, int filterIndex, int setIndex){
        for(int e : set) {
            //elementSetMap.put(e,setIndex);
            for (int i = 0; i < hashes.length; i++) {
                filters[filterIndex][getIndex(e, i)] = 1;
            }
        }
    }

    public void simulation(){
        //getting sets ready
        for(int i = 1; i < sets.length; i++){

            sets[i] = generateSet(nSetElements);
        }


        //inserting elements of each sets
        for(int i = 1; i < sets.length; i ++){
            List<Integer> selected = filterSelector(i);
            for(int s : selected){
                insertInFilter(sets[i], s, i);
            }
        }


        int count = 0;

        for(int i = 1; i < sets.length; i++){
            for(int e : sets[i]){
                int setIndex = decodeElementToSetNumber(e);
                if(setIndex == i){
                    count++;
                }
            }
        }
        //check elements
        System.out.println("Total elements correctly found " + count );


    }


    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter number of sets");
            int nSets = Integer.parseInt(reader.readLine());

            System.out.println("Please enter number of elements in each set");
            int nSetElements = Integer.parseInt(reader.readLine());

            System.out.println("Please enter number of filters");
            int nFilter = Integer.parseInt(reader.readLine());
            System.out.println("Please enter number of bits in each filter");
            int nFilterNumElements = Integer.parseInt(reader.readLine());
            System.out.println("Please enter number of hashes");
            int nHashes = Integer.parseInt(reader.readLine());

            CodedBloomFilter bloomFilter = new CodedBloomFilter(nSets,nSetElements,nFilter,nFilterNumElements,nHashes);
            bloomFilter.simulation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
