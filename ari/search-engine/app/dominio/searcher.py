class Searcher:

    def __init__(self):
        self.total_num_docs = 0
        self.document_vectors = {}

    #wij = ftij x fidj = ftij x log(d/fdj)
