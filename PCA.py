import numpy as np
from scipy.sparse.linalg import eigsh


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

    data_clone = np.delete(data_clone, np.s_[-1:], axis = 1)
    # print(np.shape(data_clone))

    covarianceMatrix = np.cov(data_clone.T)
    eigenvals, eigenvecs = eigsh(covarianceMatrix, k = 2,  return_eigenvectors=True)

    print("Vals B", eigenvals)
    print("Vecs B", eigenvecs)

    # idx = eigenvals.argsort()[-2:][::-1]
    # eigenvals = eigenvals[idx]
    # eigenvecs = eigenvecs[:,idx]
    #
    # print(covarianceMatrix)
    # print("Vals A", eigenvals)
    # print("Vecs A", eigenvecs)



calculateCoVariance()

