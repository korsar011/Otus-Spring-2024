import axios from 'axios';

const BASE_URL = '/api/genres';

export const getGenres = () => axios.get(BASE_URL);

export const getGenreById = (id) => axios.get(`${BASE_URL}/${id}`);

export const createGenre = (genre) => axios.post(BASE_URL, genre);

export const updateGenre = (id, genre) => axios.put(`${BASE_URL}/${id}`, genre);

export const deleteGenre = (id) => axios.delete(`${BASE_URL}/${id}`);