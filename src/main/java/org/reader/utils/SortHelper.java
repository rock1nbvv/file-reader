package org.reader.utils;

public class SortHelper {

    private static void swap(Long[] arr, int i, int j) {
        Long temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static int partition(Long[] arr, int low, int high) {
        Long pivot = arr[high];
        int pivotPosition = low - 1;
        for (int i = low; i <= high - 1; i++) {
            if (arr[i] < pivot) {
                pivotPosition++;
                swap(arr, pivotPosition, i);
            }
        }
        swap(arr, pivotPosition + 1, high);
        return pivotPosition + 1;
    }

    private static void quickSortInternal(Long[] arr, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(arr, low, high);
            quickSortInternal(arr, low, partitionIndex - 1);
            quickSortInternal(arr, partitionIndex + 1, high);
        }
    }

    public static void quickSort(Long[] arr) {
        quickSortInternal(arr, 0, arr.length - 1);
    }
}
