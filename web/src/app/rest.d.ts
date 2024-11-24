/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.2.1263 on 2024-11-22 19:58:55.

export interface HomeResp {
  displayName: string;
  menuItems: MenuItem[];
}

export interface MenuItem extends Comparable<MenuItem> {
  name: string;
  link: string;
  order: number;
}

export interface PageResp {
  pageContent: string;
  title: string;
}

export interface Comparable<T> {
}
