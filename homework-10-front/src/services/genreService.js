import axios from 'axios';

const BASE_URL = '/api/genres';

export const getGenres = () => axios.get(BASE_URL);

export const getGenreById = (id) => axios.get(`${BASE_URL}/${id}`);

export const createGenre = (genre) => axios.post(BASE_URL, genre);

export const updateGenre = (id, genre) => axios.put(`${BASE_URL}/${id}`, genre);

export const deleteGenre = (id) => {
    return axios.delete(`${BASE_URL}/${id}`)
        .catch(error => {
            if (error.response) {
                throw new Error(error.response.data.message || 'Cannot delete genre with associated books, kindly delete book first.');
            } else if (error.request) {
                throw new Error('No response received from server.');
            } else {
                throw new Error('Error setting up request.');
            }
        });
};