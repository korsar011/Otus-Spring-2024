import axios from 'axios';

const BASE_URL = '/api/authors';

export const getAuthors = () => axios.get(BASE_URL);

export const getAuthorById = (id) => axios.get(`${BASE_URL}/${id}`);

export const createAuthor = (author) => axios.post(BASE_URL, author);

export const updateAuthor = (id, author) => axios.put(`${BASE_URL}/${id}`, author);

export const deleteAuthor = (id) => {
    return axios.delete(`${BASE_URL}/${id}`)
        .catch(error => {
            if (error.response) {
                throw new Error(error.response.data.message || 'Cannot delete author with associated books, kindly delete book first.');
            } else if (error.request) {
                throw new Error('No response received from server.');
            } else {
                throw new Error('Error setting up request.');
            }
        });
};