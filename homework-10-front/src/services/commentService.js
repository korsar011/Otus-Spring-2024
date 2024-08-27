import axios from 'axios';

const BASE_URL = '/api/comments';

export const getCommentsByBookId = (bookId) => axios.get(`${BASE_URL}/by-book/${bookId}`);