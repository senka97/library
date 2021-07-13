import { Author } from './author';

export interface NewBook{
  title: string;
  subtitle: string;
  creationDate: Date | null;
  isbn: string;
  quantity: number;
  authors: Author[];
  imageUrl: string;
}