
import { NgStyle } from '@angular/common';
import { AfterViewInit, Component, effect, ElementRef, HostListener, Injector, input,  InputSignal, model, ModelSignal, signal, ViewChild, WritableSignal } from '@angular/core';
import { autoUpdate, computePosition, offset, shift } from '@floating-ui/dom';

@Component({
  selector: 'app-popover',
  imports: [NgStyle],
  templateUrl: './popover.component.html',
  styleUrl: './popover.component.scss'
})
export class PopoverComponent implements AfterViewInit {
  positionStyles: Record<string, string> = {};
  isOpen: ModelSignal<boolean> = model.required();
  anchorElement: InputSignal<HTMLElement>  = input.required<HTMLElement>();

  x: WritableSignal<number> = signal(0);
  y: WritableSignal<number> = signal(0);

  @ViewChild('popover')
  popover!: ElementRef<HTMLDivElement>;

  constructor(private readonly injector: Injector, private readonly elementRef: ElementRef) {}
  
  ngAfterViewInit(): void {
    this.initAutoUpdate();
  }

  initAutoUpdate() {
    let cleanAutoUpdate: () => void | undefined;
    let cleanCloseOnBlur: () => void | undefined;
    effect((onCleanup) => {
      if (this.isOpen()) {
        cleanAutoUpdate = autoUpdate(
          this.anchorElement(),
          this.popover.nativeElement,
          this.updatePosition.bind(this)
        );
      }

      onCleanup(() => {
        if (cleanAutoUpdate && !this.isOpen()) {
          cleanAutoUpdate();
        }
      });
    }, {injector: this.injector});
  }

  @HostListener('document:click', ['$event'])
  closeOnBlur(event: MouseEvent) {
    if(!this.elementRef.nativeElement.contains(event.target)) {
      this.isOpen.set(false);
    }
  }
  
  updatePosition() {
    computePosition(
      this.anchorElement(),
      this.popover.nativeElement,
      {
        placement: 'bottom',
        middleware: [offset(10), shift()],
      }
    ).then(
      ({ x, y}) => {
        this.x.set(x);
        this.y.set(y);
        this.positionStyles = {
          left: `${this.x()}px`,
          top: `${this.y()}px`,
        };
      }
    );
  }
}
