# import os
# import sys
import random
import numpy as np

m = 10
data = [map(float , i.strip().split(" ")) for i in open("features.txt").readlines()]

W = []
for i in range (m) :
    temp = []
    for j in range (len(data[0])) :
        temp.append(random.gauss(0,1))
    norm = [float(i)/sum(temp) for i in temp]
    W.append(norm)
W = np.matrix(W)

for i in range (len(W)) :
    print W[i] * W[i].T

def a(i,m):
	return 2*(math.log(math.pow(10,6)))/(m-i)
def b(i,m):
	return 2*(math.log(math.pow(10,6)))/(i)
def sigm(a,b):
	return (1/(1+math.exp(-a*b)))
def sigmder(alpha,z):
	alpha*sigm(alpha,z)(1-sigm(alpha,z))
def der(x,x1,beta,k):
	return 2*(sigm(beta,(w[:,k].T)*x)-sigm(beta,(w[:,k].T)*x1))*(sigmder(beta,(w[:,k].T)*x)*x-sigmder(beta,(w[:,k].T)*x1)*x1)


for k in range(1,m):
	for n in I1:
		for i in range(0,m-1):
			for x1 in A:
				h=h+(a(i,m)*sigm(a,dis(n,x1)-i)*(1-sigm(a,dist(n,x1)-i))*(der(n,x1,beta,k)))
			for X1 in B:
				p=p+(b(i+1,m)*sigm(b,(i+1)-dist(n,x1))*(1-sigm(b,(i+1)-dist(n,x1)))*(der(n,x1,beta,k)))
			q=h+la.p
		f=q+(ut*(((w[:,k].T)*w[:,k])-1)*w[:,k])
	w[:,k]=w[:,k]-((eta*f)/(len(I1)))
