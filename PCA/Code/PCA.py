import sys
import numpy as np
import matplotlib.pyplot as plt
from scipy.sparse.linalg import eigsh
import pandas as pd
from sklearn.manifold import TSNE
from sklearn.decomposition import TruncatedSVD


def plot(reeducedDimensions, title, diseases):
    df = pd.DataFrame(dict(x=reeducedDimensions[0], y=reeducedDimensions[1], label=diseases))

    groups = df.groupby('label')
    fig, ax = plt.subplots()
    for name, group in groups:
        ax.plot(group.x, group.y, marker='o', linestyle='', label=name)
    ax.legend()
    plt.suptitle(title)
    plt.xlabel('Principle Component 1')
    plt.ylabel('Principle Component 2')

def pca(filename, dimensions):
    np.set_printoptions(suppress=True)

    pcafile = open(filename, "r").readlines()
    diseases = [x.split("\t")[-1].split("\r")[0] for x in pcafile]
    diseases = np.array(diseases)

    data = np.genfromtxt(filename,
                          delimiter='\t'
                        )
    data_clone = np.genfromtxt(filename,
                                delimiter='\t'
                                )

    # remove last column representing diseases
    data = np.delete(data, np.s_[-1:], axis = 1)
    data_clone = np.delete(data_clone, np.s_[-1:], axis = 1)

    # PCA
    mean = np.mean(data,0)
    data_clone -= mean

    covarianceMatrix = np.cov(data_clone.T)
    eigenvals, eigenvecs = np.linalg.eig(covarianceMatrix);
    eigenvalsi = eigenvals.argsort()
    finalEigenVec = eigenvecs.T[eigenvalsi[::-1]][:dimensions]

    finalEigenVec = np.array(finalEigenVec)
    pcatransform  = np.dot(finalEigenVec, data_clone.T)
    # print(pcatransform.T)
    plot(pcatransform, "PCA on " + filename, diseases)

    svdDim = TruncatedSVD(n_components=2).fit_transform(data).T
    # print(svdDim.T)
    plot(svdDim, "SVD on " + filename, diseases)


    # t-SNE
    tsneDim = TSNE(n_components=2).fit_transform(data).T
    plot(tsneDim, "t-SNE on " + filename, diseases)
    # print(tsneDim.T)
    plt.show()


filename = sys.argv[1]
dimensions = int(sys.argv[2])
pca(filename, dimensions)

