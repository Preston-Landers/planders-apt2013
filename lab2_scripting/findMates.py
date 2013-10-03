#!/usr/bin/env python
__author__ = 'Preston Landers'
DEBUG = False

import sys


def debug(msg):
    if DEBUG:
        print(msg)


class Bear:
    def __init__(self, name, sex, mother, father, age):
        self.name = name.lower()
        self.originalName = name
        self.sex = sex
        self.mother = mother.lower()
        self.father = father.lower()
        self.age = float(age)

    def __str__(self):
        return "%(originalName)s: %(age)s year old %(sex)s bear. M/F parents: %(father)s - %(mother)s" % self.__dict__

    def __repr__(self):
        return self.originalName


class BearMater:
    def __init__(self):
        self.grandParentsDict = {} # these include parents as well
        self.offspringDict = {}
        self.bearDict = {}
        self.compatibleMates = set()

    def load(self, file_handle):
        for line in file_handle.readlines():
            if not line:
                break
            cols = line.split(":")
            name = cols[0].strip()
            sex = cols[1].strip()
            mother = cols[2].strip()
            father = cols[3].strip()
            age = cols[4].strip()
            self.bearDict[name.lower()] = Bear(name, sex, mother, father, age)

        for bearName, bear in self.bearDict.iteritems():
            grandParents = []
            for parentType in ("mother", "father"):
                parentName = getattr(bear, parentType, None)
                if parentName and parentName != "nil":
                    grandParents.append(parentName)
                    if parentName in self.bearDict:
                        parent = self.bearDict[parentName]
                        if parent.father and parent.father != "nil":
                            grandParents.append(parent.father)
                        if parent.mother and parent.mother != "nil":
                            grandParents.append(parent.mother)

            if grandParents:
                grandParents = set(grandParents)
                self.grandParentsDict[bear.name] = grandParents
                debug("BEAR %s gps: %r" % (bear.name, grandParents))
            else:
                debug("bear %s has no gps" % bear.name)

            offspring = []
            for otherBearName, otherBear in self.bearDict.iteritems():
                if otherBearName == bearName:
                    continue
                if otherBear.father == bearName or otherBear.mother == bearName:
                    offspring.append(otherBearName)
            if offspring:
                offspring = set(offspring)
                self.offspringDict[bear.name] = offspring
                debug("BEAR %s offspring: %r" % (bear.name, offspring))
            else:
                debug("BEAR %s no kids" % (bear.name,))

        return self

    def compatible(self, this, other):
        if this == other:
            return False
        if this.age < 2.0 or this.age > 6.0:
            return False
        if other.age < 2.0 or other.age > 6.0:
            return False
        if this.sex == other.sex:
            return False
        if abs(this.age - other.age) > 1.0:
            return False
        if this.name in self.offspringDict or other.name in self.offspringDict:
            return False
        thisGP = self.grandParentsDict.get(this.name, set())
        otherGP = self.grandParentsDict.get(other.name, set())
        if thisGP.intersection(otherGP):
            return False
        return True

    def solve(self):
        compatibleMates = set()
        for thisName, this in self.bearDict.iteritems():
            for otherName, other in self.bearDict.iteritems():
                if thisName == otherName:
                    continue
                if (this, other) in compatibleMates or (other, this) in compatibleMates:
                    continue
                if self.compatible(this, other):
                    compatibleMates.add((this, other))
        self.compatibleMates = compatibleMates
        return self

    def printMates(self):
        if self.compatibleMates:
            for mateSet in self.compatibleMates:
                print("{0:s} + {1:s}".format(mateSet[0].originalName, mateSet[1].originalName))


def main():
    with open(sys.argv[1], "r") as fileInput:
        BearMater().load(fileInput).solve().printMates()
    return 0


if __name__ == "__main__":
    sys.exit(main())
