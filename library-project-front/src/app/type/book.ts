import { Author } from './author';

export interface Book{
  id: number;
  title: string;
  subtitle: string;
  creationDate: Date | null;
  isbn: string;
  quantity: number;
  authors: Author[];
  imageUrl: string;
}