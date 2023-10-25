from pathlib import Path

BASE_PATH = Path()

ENGLISH_PREFIX = "en"
PORTUGUESE_PREFIX = "pt-BR"

DOCUMENT_DIR = BASE_PATH / "documents"
ENGLISH_DOC_DIR = DOCUMENT_DIR / ENGLISH_PREFIX
PORTUGUESE_DOC_DIR = DOCUMENT_DIR / PORTUGUESE_PREFIX

TRIPLE_DIR = BASE_PATH / "triples"
ENGLISH_TRIPLE_DIR = TRIPLE_DIR / ENGLISH_PREFIX
PORTUGUESE_TRIPLE_DIR = TRIPLE_DIR / PORTUGUESE_PREFIX

GRAPH_DIR = BASE_PATH / "graphs"
ENGLISH_GRAPH_DIR = GRAPH_DIR / ENGLISH_PREFIX
PORTUGUESE_GRAPH_DIR = GRAPH_DIR / PORTUGUESE_PREFIX
BLABKG_DIR = ENGLISH_GRAPH_DIR / "BlabKG"
