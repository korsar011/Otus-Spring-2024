import api from '../axiosConfig';

const BASE_URL = '/books';

export const getBooks = () => api.get(BASE_URL);

export const getBookById = (id) => api.get(`${BASE_URL}/${id}`);

export const createBook = (book) => api.post(BASE_URL, book);

export const updateBook = (id, book) => api.put(`${BASE_URL}/${id}`, book);

export const deleteBook = (id) => api.delete(`${BASE_URL}/${id}`);