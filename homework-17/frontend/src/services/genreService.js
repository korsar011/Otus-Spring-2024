import api from '../axiosConfig';

const BASE_URL = '/genres';

export const getGenres = () => api.get(BASE_URL);

export const getGenreById = (id) => api.get(`${BASE_URL}/${id}`);

export const createGenre = (genre) => api.post(BASE_URL, genre);

export const updateGenre = (id, genre) => api.put(`${BASE_URL}/${id}`, genre);

export const deleteGenre = (id) => {
    return api.delete(`${BASE_URL}/${id}`)
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