import axios from 'axios';

const BASE_URL = '/api/authors';

export const getAuthors = () => axios.get(BASE_URL);

export const getAuthorById = (id) => axios.get(`${BASE_URL}/${id}`);

export const createAuthor = (author) => axios.post(BASE_URL, author);

export const updateAuthor = (id, author) => axios.put(`${BASE_URL}/${id}`, author);

export const deleteAuthor = (id) => axios.delete(`${BASE_URL}/${id}`);