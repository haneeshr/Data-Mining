import sys
import numpy as np
import matplotlib.pyplot as plt
from scipy.sparse.linalg import eigsh
import pandas as pd
from sklearn.manifold import TSNE
from sklearn.decomposition import PCA

def plot(reeducedDimensions, colors, title):
    plt.figure()
    plt.title(title)
    plt.xlabel("Priciple Component 1")
    plt.ylabel("Priciple Component 2")
    plt.scatter(reeducedDimensions[0], reeducedDimensions[1], c=colors)

def pca(filename, dimensions):
    np.set_printoptions(suppress=True)

    pcafile = open(filename, "r").readlines()
    diseases = [x.split("\t")[-1].split("\r")[0] for x in pcafile]
    diseases = np.array(diseases)
    colors = pd.factorize(diseases)[0]

    data = np.genfromtxt(filename,
                          delimiter='\t'
                        )

    # remove last column representing diseases
    data = np.delete(data, np.s_[-1:], axis = 1)


    # PCA
    data_clone = data
    mean = np.mean(data,0)
    data_clone -= mean

    covarianceMatrix = np.cov(data_clone.T)
    eigenvals, eigenvecs = np.linalg.eig(covarianceMatrix);
    eigenvalsi = eigenvals.argsort()
    finalEigenVec = eigenvecs.T[eigenvalsi[::-1]][:dimensions]

    finalEigenVec = np.array(finalEigenVec)
    pcatransform  = np.dot(finalEigenVec, data.T)
    # print(pcatransform.T)
    # print(diseases)
    plot(pcatransform, colors, "PCA on " + filename)

    # SVD
    U,s,V = np.linalg.svd(data);
    U = U.T[:2].T
    S = np.zeros((2, 2))
    S[:2, :2] = np.diag(s[:2])
    svdDim = np.dot(U,S).T
    plot(svdDim, colors, "SVD on " + filename)


    # t-SNE
    tsneDim = PCA(n_components=2).fit_transform(data).T
    plot(tsneDim, colors, "t-SNE on " + filename)
    plt.show()


filename = sys.argv[1]
dimensions = int(sys.argv[2])
pca(filename, dimensions)

