import numpy as np
import matplotlib.pyplot as plt
from scipy.sparse.linalg import eigsh

dimensions = 2 #Number of dimensions to reduce

def calculateCoVariance():
    np.set_printoptions(suppress=True)

    pcafile = open("pca_a.txt", "r").readlines()
    diseases = [x.split("\t")[-1].split("\r")[0] for x in pcafile]
    diseases = np.array(diseases)
    data = np.genfromtxt("pca_a.txt",
                          delimiter='\t'
                        )

    rows = data.shape[0]
    cols = data.shape[1]

    data_clone = data

    mean = np.mean(data,0)

    data_clone -= mean

    data = np.delete(data, np.s_[-1:], axis = 1)
    data_clone = np.delete(data_clone, np.s_[-1:], axis = 1)

    covarianceMatrix = np.cov(data_clone.T)
    eigenvals, eigenvecs = np.linalg.eig(covarianceMatrix);

    eigenvalsi = eigenvals.argsort()
    finalEigenVec = eigenvecs[eigenvalsi[::-1]][:dimensions]

    finalEigenVec = np.array(finalEigenVec)
    pcatransform  = np.dot(finalEigenVec, data.T)
    print(pcatransform)
    print(diseases)

    unique = set()
    dict = {}
    for i in range(len(diseases)):
        if (diseases[i] not in unique):
            unique.add(diseases[i])
            dict[diseases[i]] = len(unique)

    colors = [dict[k] for k in diseases]

    plt.scatter(pcatransform[0], pcatransform[1], c=colors)

    plt.show()



calculateCoVariance()

