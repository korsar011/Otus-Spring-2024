import axios from 'axios';

const BASE_URL = '/api/books';

export const getBooks = () => axios.get(BASE_URL);

export const getBookById = (id) => axios.get(`${BASE_URL}/${id}`);

export const createBook = (book) => axios.post(BASE_URL, book);

export const updateBook = (id, book) => axios.put(`${BASE_URL}/${id}`, book);

export const deleteBook = (id) => axios.delete(`${BASE_URL}/${id}`);