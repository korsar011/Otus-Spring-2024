import api from '../axiosConfig'; 

const BASE_URL = '/authors';

export const getAuthors = () => api.get(BASE_URL);

export const getAuthorById = (id) => api.get(`${BASE_URL}/${id}`);

export const createAuthor = (author) => api.post(BASE_URL, author);

export const updateAuthor = (id, author) => api.put(`${BASE_URL}/${id}`, author);

export const deleteAuthor = (id) => {
    return api.delete(`${BASE_URL}/${id}`)
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