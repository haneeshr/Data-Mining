import numpy as np
from scipy.sparse.linalg import eigsh

dimensions = 2 #Number of dimensions to reduce

def calculateCoVariance():
    np.set_printoptions(suppress=True)


    data = np.genfromtxt("pca_a.txt",
                          delimiter='\t'
                        )

    rows = data.shape[0]
    cols = data.shape[1]

    data_clone = data
    mean = np.zeros(cols - 1)

    for i in range(cols - 1):
        for j in range(rows):
            mean[i] += data[j][i]
        mean[i] /= rows

    # print("Mean values ", mean)

    for i in range(cols - 1):
        for j in range(rows):
            data_clone[j][i] -= mean[i]

    data = np.delete(data, np.s_[-1:], axis = 1)
    data_clone = np.delete(data_clone, np.s_[-1:], axis = 1)
    # print(np.shape(data_clone))

    covarianceMatrix = np.cov(data_clone.T)
    eigenvals, eigenvecs = np.linalg.eig(covarianceMatrix);


    eigvecvalMap = []
    # print(eigenvals.size)
    for i in range(eigenvals.size):
        curreigenval = eigenvals[i]
        curreigenvec = eigenvecs[i]
        curreigvalvec = [curreigenval, curreigenvec]
        eigvecvalMap.append(curreigvalvec)

    eigvecvalMap.sort(key=lambda x: x[0], reverse=True)



    eigvecvalMap = eigvecvalMap[:dimensions]

    finalEigenVec = [eig.pop(1) for eig in eigvecvalMap]



    # print(finalEigenVec)

    finalEigenVec = np.array(finalEigenVec)
    pcatransform  = np.dot(finalEigenVec, data.T)
    print(pcatransform)




calculateCoVariance()

