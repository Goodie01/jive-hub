import {Component} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {ApiService} from '../api.service';
import {unified} from 'unified'
import rehypeStringify from 'rehype-stringify'
import remarkFrontmatter from 'remark-frontmatter'
import remarkGfm from 'remark-gfm'
import remarkParse from 'remark-parse'
import remarkRehype from 'remark-rehype'
import {ActivatedRoute} from '@angular/router';


@Component({
  selector: 'app-cust-web',
  standalone: true,
  imports: [],
  templateUrl: './cust-web.component.html',
  styleUrl: './cust-web.component.css'
})
export class CustWebComponent {
  content: string = '';

  constructor(private apiService: ApiService, private titleService:Title, private activatedRoute: ActivatedRoute) {
    activatedRoute.url.subscribe(value => {
      apiService.pageResponse(value.join("/")).subscribe(value => {
        unified()
          .use(remarkParse)
          .use(remarkFrontmatter)
          .use(remarkGfm)
          .use(remarkRehype)
          .use(rehypeStringify)
          .process(value.pageContent)
          .then(value => {
            this.content = String(value)
          })

        this.titleService.setTitle(value.title)
      })
    })

  }
}
