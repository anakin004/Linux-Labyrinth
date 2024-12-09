import sys

answers = ["cat", "cd", "..", "find", "tree", "echo", "anakin"]
inputAnswers = sys.argv[1]

def checkAnswers():
    inp = strToArr(inputAnswers)
    if len(inp) != len(answers):
        return "Error: incorrect input len"
    
    for i in range(len(answers)):
        if inp[i] != answers[i]:
            return "False"
    return "True"


def strToArr(s):
    return s.split(" ")

if __name__ == "__main__":
    print(checkAnswers())
    

